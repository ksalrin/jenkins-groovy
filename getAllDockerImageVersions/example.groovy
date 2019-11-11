import groovy.json.JsonSlurper

//
def findDockerImages(branchName) {
  def versionList = []
  def token       = ""
  def myJsonreader = new JsonSlurper()
  def nexusData = myJsonreader.parse(new URL("https://nexus.fuchicorp.com/service/rest/v1/components?repository=fuchicorp"))
  println('nexusData'.'items'.'id')
  nexusData.items.assets.downloadUrl.each() {
    if ( it[0] == "https://nexus.fuchicorp.com/repository/fuchicorp/v2/artemis-dev/manifests/0.3") {
      versionList.add(it)
    }
  }

  return versionList
}

println(findDockerImages('dev'))
