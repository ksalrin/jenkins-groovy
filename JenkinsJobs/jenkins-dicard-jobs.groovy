node {
  properties([[$class: 'BuildDiscarderProperty',
  strategy: [$class: 'LogRotator',
  artifactDaysToKeepStr: '',
  artifactNumToKeepStr: '',
  daysToKeepStr: '',
  numToKeepStr: '3'
  ]]]);

  stage('Build') {
    println("Building")
  }

  stage('Unitest') {
    println("Unitesting")
  }

  stage('Deploy') {
    println("Deploying")
  }

}
