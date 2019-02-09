

def exampleOfListFunction(item = []) {
  if (item.size() > 0) {
    item.each() {
      println("print ${it}")
    }
  }
}
def myList = [0, 1, 1, 3, 4]
// exampleOfListFunction(myList)


def exampleOfDictionaryFunction(data=[:]) {
  if (data) {
    data.each() { key, value ->
      println("User ${key} is ${value}")
    }
  }
}

def data = [:]

data.put("Anna", "kaakashka")
data.put("Farkhod", "kaakashka")


exampleOfDictionaryFunction(data)
