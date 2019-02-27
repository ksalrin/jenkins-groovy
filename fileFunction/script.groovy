import java.io.File;


File file = null
file = new File('fileFunction/example.txt')


// Checking file exist
if (file.exists()){
  println('Yep file is exist')
}


println(file.class)
