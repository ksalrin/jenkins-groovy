

def credId  = scm.getUserRemoteConfigs()[0].getCredentialsId()
String repoUrl = scm.getUserRemoteConfigs()[0].getUrl().replace('https://', '')
String dateTime = new SimpleDateFormat("yyyy/MM/dd.HH-mm-ss").format(Calendar.getInstance().getTime())


node('master') {
  checkout scm

  echo "${credId}"
  withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: "${credId}", usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
    dir("${WORKSPACE}/") {
      sh("git tag -a '${dateTime}' -m 'Jenkins'")
      sh("git push https://${env.GIT_USERNAME}:${env.GIT_PASSWORD}@${repoUrl} --tags")
    }
  }

  println("${BUILD_ID}")
}
