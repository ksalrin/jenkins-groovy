import groovy.json.JsonSlurper

def slurper = new JsonSlurper()

def object = slurper.parse(new File("json/data.json"))

object.data.each() {
  println(it.first_name)
}
