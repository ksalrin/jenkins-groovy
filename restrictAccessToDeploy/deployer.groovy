import jenkins.model.Jenkins
import hudson.model.User

allUsers = User.getAll()
adminList = []
authStrategy = Jenkins.instance.getAuthorizationStrategy()
def allowedGroup = ['admin', 'deploy', 'deployer']
allUsers.each() {
  println(it)
}

node {


  stage('test') {
    echo 'Hello'
  }
}
