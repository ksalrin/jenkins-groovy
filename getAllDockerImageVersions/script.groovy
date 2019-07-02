import groovy.json.JsonSlurper

//
def findDockerImages(branchName) {
  def versionList = []
  def token       = ""
  def myJsonreader = new JsonSlurper()
  def nexusData = myJsonreader.parse(new URL("https://nexus.fuchicorp.com/service/rest/v1/components?repository=webplatform"))
  nexusData.items.each {
    if (it.name.contains(branchName)) {
       versionList.add(it.name + ':' + it.version)
     }
    }
  while (true) {
    if (nexusData.continuationToken) {
      token = nexusData.continuationToken
      nexusData = myJsonreader.parse(new URL("https://nexus.fuchicorp.com/service/rest/v1/components?repository=webplatform&continuationToken=${token}"))
      nexusData.items.each {
        if (it.name.contains(branchName)) {
           versionList.add(it.name + ':' + it.version)
         }
        }
    }
    if (nexusData.continuationToken == null ){
      break
    }

  }
  if(!versionList) {
    versionList.add('ImmageNotFound')
  }

  return versionList.sort()
}

println(findDockerImages('qa'))
