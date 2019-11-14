@Grab('org.yaml:snakeyaml:1.17')

import org.yaml.snakeyaml.Yaml
import groovy.transform.Field

Yaml parser = new Yaml()

@Field Map nonProdYamlFile = new HashMap()
nonProdYamlFile = parser.load(("workingWithYaml/non-prod-WORKS.yaml" as File).text)
def loopThroughMethod(inputBitBucketKey, inputMap) {
  ArrayList repoArrayList = []
  inputMap.each() {
    it.each() {
      for(entry in it){
        entry.value.each() {
          def repoMap = [:]
          repoMap['bitbucketkey'] = inputBitBucketKey
          repoMap['repo'] = it.toString().split('=')[0]
          repoMap['branch'] = it.toString().split('=')[1]
          repoArrayList.add(repoMap)
        }
      }
    }
  }
  return repoArrayList
}


def getTemplates() {
  Yaml parser = new Yaml()
  def manifest =  parser.load(("workingWithYaml/non-prod-WORKS.yaml" as File).text)
  ArrayList repoArrayList = []

  repoArrayList.add(loopThroughMethod("CORE", manifest.Core.Deployment))
  repoArrayList.add(loopThroughMethod("ACCOUNT", manifest.Account.Infrastructure))
  repoArrayList.add(loopThroughMethod("ACCOUNT", manifest.Account.Deployment))
  repoArrayList.add(loopThroughMethod("MENU", manifest.Menu.Deployment))
  repoArrayList.add(loopThroughMethod("OTR", manifest.Orders.Infrastructure))
  repoArrayList.add(loopThroughMethod("OTR", manifest.Orders.Deployment))
  repoArrayList.add(loopThroughMethod("OFFERS", manifest.Offers.Deployment))

  return repoArrayList
}

project = getTemplates()

project.each() {
  it.each() {
    println(it)
  }
}
