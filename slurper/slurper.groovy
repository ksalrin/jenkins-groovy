import groovy.json.JsonSlurper

def myJsonreader = new JsonSlurper()

def data = myJsonreader.parseText('[{"name": "anna"}, {"name": "beki"}]')

def mydata = myJsonreader.parse(new File("json/data.json"))

def myurl = myJsonreader.parse(new URL("http://fscoding.com/api/example-users"))

if ("Anna"  in data.name ){
  println('Anna is present')
} else {
  println("Anna is missing !")
}


myurl.data.each() {
  if (it.first_name == "Charles")
    println("Charles is present")
}

// println(myurl)
