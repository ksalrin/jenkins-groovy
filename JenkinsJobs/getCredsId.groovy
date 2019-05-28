

def credId  = scm.getUserRemoteConfigs()[0].getCredentialsId()

def repo = 'https://github.com/fsadykov/keep-creds.git'.replace('https//', '')

node('master') {
  echo "${credId}"
  withCredentials([[$class: 'UsernamePasswordMultiBinding',
  credentialsId: "${credId}",
  usernameVariable: 'GIT_USERNAME',
  passwordVariable: 'GIT_PASSWORD']]) {
    sh("git clone https://${env.GIT_USERNAME}:${env.GIT_PASSWORD}@${repo}")
  }
}
