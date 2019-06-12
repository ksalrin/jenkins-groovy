

node('master') {

  properties([[$class: 'RebuildSettings',
  autoRebuild: false,
  rebuildDisabled: false],
  pipelineTriggers([cron('''41 15 * * *''')])])
  stage('poll Code') {

    checkout scm
  }

  stage('cheking') {
    sh "ls ${WORKSPACE}"
  }

}
