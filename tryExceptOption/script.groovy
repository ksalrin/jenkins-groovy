
try {

  def users = ['Abdul', 'Farkhod', 'Ahmad']
  users.each { 
    println("User: ${it}")
  }

} catch(err) {
  println("Something happend ${err}")
}
