import groovy.json.JsonSlurper

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
  println(getTeamId)
  def totalUsers = []
  def memberUrl = ""
  def pageCount = 1

  while (true) {
    // While loop to go each pages and get all members from team 
    memberUrl = "https://api.github.com/teams/${getTeamId}/members?page=${pageCount}"
    def get = new URL(memberUrl).openConnection();
      get.setRequestMethod("GET")
      get.setRequestProperty("Authorization", "token ${gitToken}")
      get.setRequestProperty("Content-Type", "application/json")
     def object = jsonSlurper.parseText(get.getInputStream().getText())

    //  Braking the while loop when no one found in the page
     if (! object.login) {
       break;
     }

    // Adding list of found people to totalUsers
    object.login.each{ totalUsers.add(it) }
    pageCount = pageCount + 1
  }
  return totalUsers
}


println("DevOps Members: ${getTeamMembers('devops')}")
// println("Admin Members: ${getTeamMembers('admin')}")
// println("Members: ${getTeamMembers('members')}")
