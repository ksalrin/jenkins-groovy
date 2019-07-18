def marketList = ["ca", "de", "hk", "au", "uk", "us"]

String param = 'ca'


if (param) {
  println('Working')
}

marketList.each() {
  if (param.contains(it)) {
    println('Works')
  }
}
