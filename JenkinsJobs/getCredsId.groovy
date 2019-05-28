

def credId  = scm.getUserRemoteConfigs()[0].getCredentialsId()

// def repo = 'https://github.com/fsadykov/keep-creds.git'.replace('https://', '')


String repoUrl = project.getDefinition().getScm().getUserRemoteConfigs()[0].getUrl()

node('master') {

  echo "${credId}"
  withCredentials([[$class: 'UsernamePasswordMultiBinding',
  credentialsId: "${credId}",
  usernameVariable: 'GIT_USERNAME',
  passwordVariable: 'GIT_PASSWORD']]) {
    dir("${WORKSPACE}/") {
      sh 'rm -rf keep-creds'
      sh("git clone https://${env.GIT_USERNAME}:${env.GIT_PASSWORD}@${repoUrl} ")
      sh "ls -la "
    }
  }
}
