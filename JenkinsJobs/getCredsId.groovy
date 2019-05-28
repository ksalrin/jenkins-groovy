

def credId  = scm.getUserRemoteConfigs()[0].getCredentialsId()
String repoUrl = scm.getUserRemoteConfigs()[0].getUrl().replace('https://', '')

node('master') {

  echo "${credId}"
  withCredentials([[$class: 'UsernamePasswordMultiBinding',
  credentialsId: "${credId}",
  usernameVariable: 'GIT_USERNAME',
  passwordVariable: 'GIT_PASSWORD']]) {
    dir("${WORKSPACE}/") {
      sh("git tag -a some_tag -m 'Jenkins'")
      sh("git push https://${env.GIT_USERNAME}:${env.GIT_PASSWORD}@${repoUrl} --tags")
    }
  }
}
