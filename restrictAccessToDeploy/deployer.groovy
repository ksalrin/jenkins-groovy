import com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy



node {
  def authStrategy = Jenkins.instance.getAuthorizationStrategy()
  stage('test') {
    echo 'Hello'
  }
}
