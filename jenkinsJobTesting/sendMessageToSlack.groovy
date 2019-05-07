#!/usr/bin/env groovy

fuchiCorpSlackUrl = 'https://fuchicorp.slack.com/services/hooks/jenkins-ci/'
slackTokenId = 'slack-token'
def salckChannel = 'test-message'



node {
  stage('notifyStarted') {
    notifyStarted()
  }

  stage('notifySuccessful') {
    notifySuccessful()
  }

  stage('notifyFailed()') {
    notifyFailed()
  }
}


def notifyStarted() {
    slackSend (color: '#FFFF00', baseUrl : "${fuchiCorpSlackUrl}".toString(), tokenCredentialId: "${slackTokenId}".toString(),
    message: """
    Please add let team know if this is mistake or please send an email

    STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL}).
    email: fuchicorpsolution@gmail.com

    """)
}

def notifySuccessful() {
    slackSend (color: '#00FF00', baseUrl : "${fuchiCorpSlackUrl}".toString(), tokenCredentialId: "${slackTokenId}".toString(),
    message: """
    Jenkins Job was successfully built.

    SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})
    email: fuchicorpsolution@gmail.com

    """)
}

def notifyFailed() {
    slackSend (color: '#FF0000', baseUrl : "${fuchiCorpSlackUrl}".toString(),  tokenCredentialId: "${slackTokenId}".toString(),
    message: """
    Jenkins build is breaking for some reason. Please go to job and take actions.

    FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    email: fuchicorpsolution@gmail.com

    """)
}
