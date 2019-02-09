
// Import the module
import groovy.json.*
def jsonSlurper = new JsonSlurper()

// Open the json file read
def reader = new BufferedReader(
  new InputStreamReader(
    new FileInputStream("json/data.json"),"UTF-8")
    )

//  readen data convert to groovy object
data = jsonSlurper.parse(reader)

// Go to each user and print  fist and last name
data["data"].each() {
  println("Users first name :${it['first_name']}, Last name :${it['last_name']}")
}
