def deploymentName = 'PROD'


if (deploymentName.toLowerCase() != "prod") {
  println("Not Working")
} else {
  println("Working")
}
