import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

// def jsonSlurper = new JsonSlurper()
// def object = jsonSlurper.parseText('{ "name": "John Doe" } /* some comment */')
//
// assert object instanceof Map
// // assert object.name == 'John Doe'
// println(object.name)
//
//
//
data = """
{
    "ImageId": "ami-0e8e73017e19cf62c"
}
"""


// def newAmi  = jsonSlurper.parseText(
//   sh(returnStdout: true, script: "aws ec2 create-image --instance-id ${} --name ${}"))

def data = new groovy.json.JsonSlurper().parseText(data).ImageId
// data = jsonSlurper.parseText(data)

println(data)
