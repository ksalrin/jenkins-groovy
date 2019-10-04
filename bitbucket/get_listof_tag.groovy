import groovy.json.JsonSlurper

def myJsonreader = new JsonSlurper()
def  endpoint = 'bitbucket.sharedtools.vet-tools.digitalecp.mcd.com/rest/api/1.0/projects/config/repos/configurations/tags'
def tagList  = ''

"Content-Type: application/json" -H "Authorization: Bearer MzcwNzY2ODI1Mzg2OvFZER6qTtspARhUeo1I04+qmTj/"  "bitbucket.sharedtools.vet-tools.digitalecp.mcd.com/rest/api/1.0/projects/config/repos/configurations/tags"

def json = "http://www.example.com/api/myresouce".toURL().
    getText(requestProperties: [Accept: 'application/json', Authorization: "Bearer MzcwNzY2ODI1Mzg2OvFZER6qTtspARhUeo1I04+qmTj/" ])
println(json)


//endpoint = "https://${username}:${password}@${URL}"
//tagList = myJsonreader.parse(new URL("${endpoint}"))
//println(tagList)
