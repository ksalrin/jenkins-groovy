@Grab('org.yaml:snakeyaml:1.17')

import org.yaml.snakeyaml.Yaml
import groovy.transform.Field

Yaml parser = new Yaml()

@Field Map nonProdYamlFile = new HashMap()
nonProdYamlFile = parser.load(("workingWithYaml/non-prod-WORKS.yaml" as File).text)

def functionListOfRepo() {
  ArrayList arrayList = []

  nonProdYamlFile.Core.Deployment.GLS_Pre.each() {
    def repoMap = [:]
    repoMap['repo'] = it.toString().split('=')[0]
    repoMap['branch'] =  it.toString().split('=')[1]

    arrayList.add(repoMap)
  }
  return arrayList
}

def repoMap = functionListOfRepo()

repoMap.each() {
  println(it.get('repo'))
  println(it.get('branch'))
}
