try {
    def name = "John"
    assert name == "Peter" : "Name should be John"
} catch (AssertionError e) {
	println "Something bad happened: " + e.getMessage()
}
