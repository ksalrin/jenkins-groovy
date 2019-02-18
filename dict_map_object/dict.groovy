
UUID.randomUUID().toString().replace('-', '')
// Generate the string to using uqnuq nunber
def person = [:]
person['ID'] = UUID.randomUUID().toString()
person['fname'] = 'Farrukh'
person['lname'] = 'Sadykov'
person['number'] = ['+1 (773) 328-9350', '+1 (872) 772-0715 ']
println("User\'s ID ${person['ID']}")
println("User\'s the first name is: ${person['fname']}")
println("User\'s the last name is: ${person['lname']}")
println("User\'s the phonenumbers is: ${person['number']}")
