@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.*

Yaml parser = new Yaml()
manifest = parser.load(("workingWithYaml/non-prod-WORKS.yaml" as File).text)

ArrayList repoArrayList = []

manifest.Core.Infrastructure.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
}

manifest.Core.Deployment.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
}

manifest.Account.Infrastructure.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
}

manifest.Account.Deployment.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
}

manifest.Menu.Deployment.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
}

manifest.Orders.Infrastructure.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
}

manifest.Orders.Deployment.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
}

manifest.Offers.Deployment.each() {
        it.each(){
            for(entry in it){
                entry.value.each(){
                    def repoMap = [:]
                    repoMap['repo'] = it.toString().split('=')[0]
                    repoMap['branch'] = it.toString().split('=')[1]
                    repoArrayList.add(repoMap)
                }
            }
        }
}

println(repoArrayList)

repoArrayList.each() {
  println(it.get('repo'))
}

println("orders/otr-dotnetcore-container-orderms")
