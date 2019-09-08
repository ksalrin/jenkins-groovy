import groovy.json.JsonSlurper


def findDockerImages(nameApp) {
  def foundRepo    = ""
  def token        = ""
  def myJsonreader = new JsonSlurper()
  def nexusData = myJsonreader.parse(new URL("https://registry.hub.docker.com/v2/repositories/fuchicorp/"))
  def repoVersions = []

  nexusData.results.each {
    if (it.name ==  nameApp ) {
        foundRepo = it.name
      }
    }

  nexusData = myJsonreader.parse(new URL("https://registry.hub.docker.com/v2/repositories/fuchicorp/${foundRepo}/tags/"))

  nexusData.results.each {
    repoVersions.add( "${foundRepo}:" +  it.name)
  }

  if(!repoVersions) {
    versionList.add('ImmageNotFound')
  }

  return repoVersions.sort()
}

println(findDockerImages('fuchicorp-monitor'))
