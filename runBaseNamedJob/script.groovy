

node('master') {
  def jobName = "${JOB_NAME}"
        .toLowerCase()
        .replace(" ", "-")
        .replace(".net", "dotnet")

  println(jobName)

  if (jobName.contains('base')) {
    properties([[$class: 'RebuildSettings',
    autoRebuild: false,
    rebuildDisabled: false],
    pipelineTriggers([cron('''56 20 * * *''')])])
  }

  stage('poll Code') {

    checkout scm
  }

  stage('cheking') {
    sh "ls ${WORKSPACE}"
  }


}
