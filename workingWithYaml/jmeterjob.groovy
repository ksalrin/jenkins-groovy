@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.*
import groovy.transform.Field

Yaml parser = new Yaml()
manifest = parser.load(("workingWithYaml/non-prod-WORKS.yaml" as File).text)

def getResult() {

}
@Field ArrayList repoArrayList = []

def loopThroughMethod(inputBitBucketKey, inputMap) {
    inputMap.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['bitbucketkey'] = inputBitBucketKey
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
    }
}

loopThroughMethod("CORE", manifest.Core.Deployment)
loopThroughMethod("ACCOUNT", manifest.Account.Infrastructure)
loopThroughMethod("ACCOUNT", manifest.Account.Deployment)
loopThroughMethod("MENU", manifest.Menu.Deployment)
loopThroughMethod("OTR", manifest.Orders.Infrastructure)
loopThroughMethod("OTR", manifest.Orders.Deployment)
loopThroughMethod("OFFERS", manifest.Offers.Deployment)

println(repoArrayList)
