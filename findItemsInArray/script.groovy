def marketList = ["ca", "de", "hk", "au", "uk", "us"]

String param = 'ca'


marketList.each() {
  if (param.contains(it)) {
    println('Yes item is in array')
  }
}
