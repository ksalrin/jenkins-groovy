import jenkins.model.Jenkins
import hudson.security.SecurityRealm
import org.jenkinsci.plugins.GithubSecurityRealm
import net.sf.json.JSONObject

if(!binding.hasVariable('github_realm')) {
    github_realm = [:]
}

if(!(github_realm instanceof Map)) {
    throw new Exception('github_realm must be a Map.')
}

String git_hub_auth_id  = "${jenkins_auth_client_id}"
String git_hub_auth_secret  = "${jenkins_auth_secret}"

github_realm = github_realm as JSONObject

String githubWebUri = github_realm.optString('web_uri', GithubSecurityRealm.DEFAULT_WEB_URI)
String githubApiUri = github_realm.optString('api_uri', GithubSecurityRealm.DEFAULT_API_URI)
String oauthScopes = github_realm.optString('oauth_scopes', GithubSecurityRealm.DEFAULT_OAUTH_SCOPES)
String clientID = github_realm.optString('client_id', git_hub_auth_id)
String clientSecret = github_realm.optString('client_secret', git_hub_auth_secret)

if(!Jenkins.instance.isQuietingDown()) {
    if(clientID && clientSecret) {
        SecurityRealm github_realm = new GithubSecurityRealm(githubWebUri, githubApiUri, clientID, clientSecret, oauthScopes)
        if(!github_realm.equals(Jenkins.instance.getSecurityRealm())) {
            Jenkins.instance.setSecurityRealm(github_realm)
            Jenkins.instance.save()
            println 'Security realm configuration has changed.  Configured GitHub security realm.'
        } else {
            println 'Nothing changed.  GitHub security realm already configured.'
        }
    }
} else {
    println 'Shutdown mode enabled.  Configure GitHub security realm SKIPPED.'
}
