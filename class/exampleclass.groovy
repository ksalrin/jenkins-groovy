

def dataList = []

dataList.add('Anna')
dataList.add('Ahmed')
dataList.add('Roman')
dataList.add('Sofya')

class myExampleClass {
   static void main(String[] args) {
      // Using a simple println statement to print output to the console
      println('Hello World');
   }

   def printName(name) {
     println("Hello ${name}")
   }

   def printFromList(list) {
     list.each() {
       println( "Hello ${it}")
     }
   }

}

// new myExampleClass().printName('Farkhod')

new myExampleClass().printFromList(dataList)
