
import groovy.json.JsonSlurper

def jsonReader = new JsonSlurper()

Object persons = ['Abdul', 'Farkhod', 'Farrukh', 'Nazira', 'Nazira']
persons.each() {
  println(it)
}

if (persons[3].any()){
  println("4 user is exists")
}

println(persons.equals(['Abdul', 'Farkhod', 'Farrukh', 'Nazira']))
println(persons.count('Nazira'))

String result
println(persons.each { result += it + ',' })

if ('Farkhod' in persons) {
  println('Works')
}

def dataFromUrl = jsonReader.parse( new URL('http://fscoding.com/api/example-users') )

println(result.replace('null', ''))
