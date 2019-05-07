import groovy.json.JsonSlurper

def myJsonreader = new JsonSlurper()

// def myurl = myJsonreader.parse(new URL("http://fuchicorp.com/api/users"))

public class Employee {
  private String firstName;
  private String lastName;
  private String eMail;
  private boolean status;
  private def myJsonreader = new JsonSlurper();

  void works(){
    println("Hello $firstName")
  }

  // Method will go to fuchicorp and get users first and just print
  void showAllUsers() {
    def myurl = myJsonreader.parse(new URL("http://fuchicorp.com/api/users"))
    myurl.data.each() {
      println(it.first_name)
    }
  }

  /**
  * Write a method to find user from base
  * if user is exist print Users is exist!
  * if user is not exist in database print does not exist
  */
  void isUserExist(String users) {
    def myurl = myJsonreader.parse(new URL("http://fuchicorp.com/api/users"))

    if ( myurl.data.first_name.contains(users) ) {
      println('Users is exist!') } else { println('Users is does not exist') }
  }
}


Employee data = new Employee()
data.firstName = 'Farkhod'
data.lastName = 'Sadykov'

data.works()
data.showAllUsers()
data.isUserExist('Charles')
data.isUserExist('Pika')
