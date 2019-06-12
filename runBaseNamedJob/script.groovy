

node('master') {

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
