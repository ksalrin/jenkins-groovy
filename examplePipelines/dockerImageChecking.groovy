import groovy.json.JsonSlurper

def mapedData = [:]

node {

  def version = '5.0.4'
  def newVersion = 0.8
  def mapdata.data = 'Example Data'
  stage('Check') {
    def jsonReader = new JsonSlurper()
    def dataFromUrl = jsonReader.parse( new URL('http://docker.fuchicorp.com/service/rest/v1/components?repository=fscoding') )
    def versions = dataFromUrl.items.version

    if (dataFromUrl.items.version.contains(newVersion)) {
      println('I am not going to deploy')
    } else {
      println("Shared variable ${version}")
      println("Integer sharing ${newVersion}")
    }
  }
}
