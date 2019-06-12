

node('master') {
  def jobName = "${JOB_NAME}"
        .toLowerCase()
        .replace(" ", "-")
        .replace(".net", "dotnet")

  println(jobName)

  if (jobName.contains('base')) {
    println('Hello works')
  }

  properties([[$class: 'RebuildSettings',
  autoRebuild: false,
  rebuildDisabled: false],
  pipelineTriggers([cron('''47 20 * * *''')])])
  stage('poll Code') {

    checkout scm
  }

  stage('cheking') {
    sh "ls ${WORKSPACE}"
  }

}
