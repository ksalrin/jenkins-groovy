import groovy.json.JsonSlurper


// # create a function with argument username
// # function should find username from link
// # http://fuchicorp.com/api/users


def findUser(username) {

  def userList = []
  def myJsonreader = new JsonSlurper()
  def nexusData = myJsonreader.parse(new URL("http://fuchicorp.com/api/users"))

  nexusData.data.each {
    if (it.first_name.contains(username)) {
      userList.add(it)
    }
  }

  return userList
}
println(findUser('Charles'))

node {
  properties([parameters([
    choice(choices: findUser('Charles'), description: '', name: '')
    ])])

}
// println(findUser('awdawd'))
// choice(name: 'SelectedDockerImage', choices: findDockerImages(branch), description: 'Please select docker image to deploy!')
