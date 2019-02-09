def data = [
   "fname": "Anna",
   "lname": "Malanova",
   "username" : "annachka"]

try {
  assert data.fname == "anna" : "Name should be Upercase Anna"
  } catch (AssertionError e) {
  	println "Something bad happened: " + e.getMessage()
}
