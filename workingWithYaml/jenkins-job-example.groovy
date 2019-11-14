@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.*
import groovy.transform.Field



def loopThroughMethod(inputBitBucketKey, inputMap) {
  ArrayList repoArrayList = []
  inputMap.each() {
    it.each() {
      for(entry in it){
        entry.value.each() {
          def repoMap = [:]
          repoMap['bitbucketkey'] = inputBitBucketKey
          repoMap['repo'] = it.toString().split('=')[0]
          if (it.toString().split('=')[1] == 'default') {
            repoMap['branch'] = 'develop'

          } else if(it.toString().split('=')[1] == "bugfix-push-button-deploy") {
            repoMap['branch'] = "bugfix/push-button-deploy"

            } else {
            repoMap['branch'] = it.toString().split('=')[1]
          }
          repoArrayList.add(repoMap)
        }
      }
    }
  }
  return repoArrayList
}

def getTemplates() {
  ArrayList repoArrayList = []
  Yaml parser = new Yaml()
  manifest = parser.load(("workingWithYaml/non-prod-WORKS.yaml" as File).text)


  repoArrayList.add(loopThroughMethod("core", manifest.Core.Deployment))
  repoArrayList.add(loopThroughMethod("accounts", manifest.Account.Infrastructure))
  repoArrayList.add(loopThroughMethod("accounts", manifest.Account.Deployment))
  repoArrayList.add(loopThroughMethod("menu", manifest.Menu.Deployment))
  repoArrayList.add(loopThroughMethod("otr", manifest.Orders.Infrastructure))
  repoArrayList.add(loopThroughMethod("otr", manifest.Orders.Deployment))
  repoArrayList.add(loopThroughMethod("offers", manifest.Offers.Deployment))

  return repoArrayList
}

println(getTemplates())
