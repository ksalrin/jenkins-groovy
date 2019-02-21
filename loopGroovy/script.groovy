import groovy.json.JsonSlurper

def jsonReader = new JsonSlurper()

def dataFromUrl = jsonReader.parse( new URL('http://fscoding.com/api/example-users') )

def users = dataFromUrl.data.first_name

// First each loop example
users.each {
  println(it)
}
println('###########################')
users.each { username ->
  println(username)
}
println('###########################')

// Secong how times loop which is very useful

dataFromUrl.each { key, value ->
  if (key == 'data'){
    value.first_name.count('Tracey').times{
      println('How many Tracey exist in this data :' + ++it)
    }
  }
}
