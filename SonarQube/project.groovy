
import groovy.json.JsonSlurper


env = System.getenv()
jsonSlurper = new JsonSlurper()
sonarUser = [:]


// Getting SonarQube url to be able to connect to server
if (env.SONARQUBE_URL) {
    // export SONARQUBE_URL=https://sonarqube.fuchicorp.com
    url = "${env.SONARQUBE_URL}"
} else {
    println("<SONARQUBE_URL> not found!!")
    System.exit(1)
}

// Getting the username and password
if (env.ADMIN_PASSWORD || env.ADMIN_PASSWORD) {
    sonarUser["username"] = env.ADMIN_USER
    sonarUser["password"] = env.ADMIN_PASSWORD
} else {
    println("<ADMIN_USER> or <ADMIN_PASSWORD> not found!!")
    System.exit(1)
}

// Create Project Function
def createProject(projectKey, projectName, sonarUser) {
    def data = new URL("${url}/api/projects/create?project=${projectKey}&name=${projectName}").openConnection();
        data.setRequestMethod("POST")
        data.setRequestProperty("Content-Type", "application/json")
    
    if (data.responseCode == 400 ) {
        return ["message": "Project with name <${projectKey}> already exist"]
    } else {
        def responseData = jsonSlurper.parseText(data.getInputStream().getText())
        return responseData
    }
}

// Generating the token to run the sonar scan
def genToken(tokenName) {
    def data = new URL("${url}/api/user_tokens/generate?name=${tokenName}").openConnection();
        data.setRequestProperty("Authorization", "Basic " + "${sonarUser['username']}:${sonarUser['password']}".bytes.encodeBase64().toString())
        data.setRequestProperty("Content-Type", "application/json")
        data.setRequestMethod("POST")
    
    if (data.responseCode == 400 ) {
        return ["message": "Token with name <${tokenName}> already exist"]
    } else {
        def responseData = jsonSlurper.parseText(data.getInputStream().getText())
        return responseData
    }
}


def example = ["token":"awdawdawdawd"]

println("""
hello ${example.token}
""")
println(url)
println("Basic " + "${sonarUser['username']}:${sonarUser['password']}".bytes.encodeBase64().toString())
// println(genToken("eo").token.toString())
// println(createProject("example", "example", sonarUser))