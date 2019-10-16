import hudson.*
import hudson.security.*
import jenkins.model.*
import java.util.*
import com.michelin.cio.hudson.plugins.rolestrategy.*
import com.synopsys.arc.jenkins.plugins.rolestrategy.*
import java.lang.reflect.*
import java.util.logging.*
import groovy.json.*
import groovy.json.JsonSlurper

def env = System.getenv()
jsonSlurper = new JsonSlurper()
gitToken = System.getenv().get('GIT_TOKEN')

def getTeamId(teamName) {
  /*
   Function to find teams ID
  */
  def organizationName = 'fuchicorp'
  def teamsUrl = "https://api.github.com/orgs/${organizationName}/teams"
  def teamId = null

  def get = new URL(teamsUrl).openConnection();
      get.setRequestMethod("GET")
      get.setRequestProperty("Authorization", "token ${gitToken}")
      get.setRequestProperty("Content-Type", "application/json")

  def data = jsonSlurper.parseText(get.getInputStream().getText())

  data.each() {
    if (it.name.toLowerCase() == teamName.toLowerCase()) {
      teamId = it.id
    }
  }

  return teamId
}


def getTeamMembers(teamName) {

  /*
  Function to find team members from github
  */

  def getTeamId = getTeamId(teamName)
  def memberUrl = "https://api.github.com/teams/${getTeamId}/members"
  def get = new URL(memberUrl).openConnection();
      get.setRequestMethod("GET")
      get.setRequestProperty("Authorization", "token ${gitToken}")
      get.setRequestProperty("Content-Type", "application/json")

  def object = jsonSlurper.parseText(get.getInputStream().getText())
  return object.login

}

println("DevOps Members: ${getTeamMembers('devops')}")
println("Admin Members: ${getTeamMembers('admin')}")
println("Members: ${getTeamMembers('members')}")

def devopTeam = getTeamMembers('devops')
def orgMembers = getTeamMembers('members')
/**
 * ===================================
 *
 *                Roles
 *
 * ===================================
 */
def globalRoleRead = "read"
def globalBuildRole = "build"
def globalRoleAdmin = "admin"

/**
 * ===================================
 *
 *           Users and Groups
 *
 * ===================================
 */
def access = [
  admins: devopTeam,
  builders: [],
  readers: orgMembers
]

if (env.AUTHZ_JSON_FILE)  {
  println "Get role authorizations from file ${env.AUTHZ_JSON_FILE}"
  File f = new File(env.AUTHZ_JSON_FILE)
  def jsonSlurper = new JsonSlurper()
  def jsonText = f.getText()
  access = jsonSlurper.parseText( jsonText )
}
else if (env.AUTH_JSON_URL) {
  println "Get role authorizations from URL ${env.AUTHZ_JSON_URL}"
  URL jsonUrl = new URL(env.AUTHZ_JSON_URL);
  access = new JsonSlurper().parse(jsonUrl);
}
else {
  println "Warning! Neither env.AUTHZ_JSON_FILE nor env.AUTHZ_JSON_URL specified!"
  println "Granting anonymous admin access"
}

/**
 * ===================================
 *
 *           Permissions
 *
 * ===================================
 */

// TODO: drive these from a config file
def adminPermissions = [
"hudson.model.Hudson.Administer",
"hudson.model.Hudson.Read"
]

def readPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Discover",
"hudson.model.Item.Read"
]

def buildPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Build",
"hudson.model.Item.Cancel",
"hudson.model.Item.Read",
"hudson.model.Run.Replay"
]

def roleBasedAuthenticationStrategy = new RoleBasedAuthorizationStrategy()
Jenkins.instance.setAuthorizationStrategy(roleBasedAuthenticationStrategy)


/**
 * ===================================
 *
 *               HACK
 * Inspired by https://issues.jenkins-ci.org/browse/JENKINS-23709
 * Deprecated by on https://github.com/jenkinsci/role-strategy-plugin/pull/12
 *
 * ===================================
 */

Constructor[] constrs = Role.class.getConstructors();
for (Constructor<?> c : constrs) {
  c.setAccessible(true);
}

// Make the method assignRole accessible
Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", RoleType.class, Role.class, String.class);
assignRoleMethod.setAccessible(true);
println("HACK! changing visibility of RoleBasedAuthorizationStrategy.assignRole")

/**
 * ===================================
 *
 *           Permissions
 *
 * ===================================
 */

Set<Permission> adminPermissionSet = new HashSet<Permission>();
adminPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    adminPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> buildPermissionSet = new HashSet<Permission>();
buildPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    buildPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> readPermissionSet = new HashSet<Permission>();
readPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    readPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

/**
 * ===================================
 *
 *      Permissions -> Roles
 *
 * ===================================
 */

// admins
Role adminRole = new Role(globalRoleAdmin, adminPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, adminRole);

// builders
Role buildersRole = new Role(globalBuildRole, buildPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, buildersRole);

// anonymous read
Role readRole = new Role(globalRoleRead, readPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, readRole);

/**
 * ===================================
 *
 *      Roles -> Groups/Users
 *
 * ===================================
 */

access.admins.each { l ->
  println("Granting admin to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, adminRole, l);
}

access.builders.each { l ->
  println("Granting builder to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, buildersRole, l);
}

access.readers.each { l ->
  println("Granting read to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, readRole, l);
}

Jenkins.instance.save()
