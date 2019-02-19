
File propertiesFile = new File('examples/examplSentens')
propertiesFile.withInputStream {
    println(it)
}
