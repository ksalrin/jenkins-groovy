import jenkins.model.Jenkins
import hudson.model.User

allUsers = User.getAll()
adminList = []
authStrategy = Jenkins.instance.getAuthorizationStrategy()
def allowedGroup = ['admin', 'deploy', 'deployer']

node {
  allUsers.each() {
    println(it)
  }

  stage('test') {
    echo 'Hello'
  }
}
