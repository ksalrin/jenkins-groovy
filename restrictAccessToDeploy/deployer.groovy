import com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy

def authStrategy = Jenkins.instance.getAuthorizationStrategy()

node {

  stage('test') {
    echo 'Hello'
  }
}
