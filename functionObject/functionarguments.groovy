def myFunction(s3bucket, environment=false) {
  println(s3bucket)
  // if (data == false ) {
  //   println('Function does not have anything ')
  //
  // } else {
  //  println("Function parsed: ${data}")
  // }
  def iamAWSRole = ''
  switch(environment) {
    case 'prod':
      iamAWSRole = "arn:aws:iam::988602466528:role/JenkinsSlavePodsRole"
    break

    case 'stage':
      iamAWSRole = "arn:aws:iam::674982954500:role/JenkinsSlavePodsRole"
    break

    case 'qa':
      iamAWSRole = "arn:aws:iam::284422666245:role/JenkinsSlavePodsRole"
    break

    default:
      iamAWSRole = "arn:aws:iam::607546651489:role/JenkinsSlavePodsRole"
    break
  }

  println(iamAWSRole)
}


myFunction('example', 'qa')
