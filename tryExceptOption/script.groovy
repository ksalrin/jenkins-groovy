
try {

  def users = ['Abdul', 'Farkhod', 'Ahmad']
  users.each {
    println("User: ${it}")
  }

} catch(err) {
  println("Something happend ${err}")
}


println("jenkins-pipeline-${UUID.randomUUID().toString()}")



println("jenkins-pipeline-${UUID.randomUUID().toString().split('-')[0]}")


def data = 'webplatform-fuchicorp-deploy/fsadykov'
branchName = data.split('/').last()

println(branchName)
