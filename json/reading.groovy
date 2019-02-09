import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText('{ "name": "John Doe" } /* some comment */')

assert object instanceof Map
// assert object.name == 'John Doe'
println(object.name)
