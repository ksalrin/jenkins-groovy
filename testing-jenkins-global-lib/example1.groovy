

def credId  = scm.getUserRemoteConfigs()[0].getCredentialsId()


node('master') {
  echo "${credId}"
}
