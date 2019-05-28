import groovy.json.JsonSlurper

//
def findDockerImages(branchName) {
  def versionList = []
  def myJsonreader = new JsonSlurper()
  def nexusData = myJsonreader.parse(new URL("https://nexus.fuchicorp.com/service/rest/v1/components?repository=webplatform"))
  nexusData.items.each {
    if (it.name.contains(branchName)) {
       versionList.add(it.name + ':' + it.version)
     }
    }


  if (versionList.isEmpty()) {
    return ['ImageNotFound']
  }

  return versionList
}

println(findDockerImages('qa'))
// choice(name: 'SelectedDockerImage', choices: findDockerImages(branch), description: 'Please select docker image to deploy!')
