def scmCredsID
node {
  scmCredsID = scm.getUserRemoteConfigs()[0].getCredentialsId()
  println(scmCredsID)
}
