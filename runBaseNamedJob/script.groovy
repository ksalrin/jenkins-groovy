

node('master') {

  properties([[$class: 'RebuildSettings',
  autoRebuild: false,
  rebuildDisabled: false],
  pipelineTriggers([cron('''0 16 * * *''')])])
  stage('poll Code') {

    checkout scm
  }

  stage('cheking') {
    sh "ls ${WORKSPACE}"
  }

}
