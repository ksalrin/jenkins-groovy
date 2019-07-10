
def commonSendWebexAlert(String baseName, String jobName) {
  if (baseName.contains('base') ) {
    if (jobName == 'master' || jobName == 'develop') {
      def body= """{
            "name": "${baseName}",
            "url": "${JOB_URL}",
            "build": {
                "full_url": "${BUILD_URL}",
                "number": "${BUILD_NUMBER}",
                "phase": "COMPLETED",
                "status": "FAILURE",
                "url": "${BUILD_URL}",
                "scm": {
                    "url": "${GIT_URL}",
                    "branch": "${GIT_BRANCH}",
                    "commit": "${GIT_COMMIT}"
                }
            }
        }"""
        sh  "curl -X POST -H 'Content-Type: application/json' 'https://botworkflows.webex.com/embed/run/99602d28c0d16bae0f21b2' -d '${body}'"
    }
  }
}

node {
   withEnv(["GIT_URL=https://github.com/fsadykov/jenkins-groovy.git",
            "GIT_BRANCH=master",
            "GIT_COMMIT=34uib23o3oirnor3i2rn23orinoi23nofonc"]) {
      stage('Send notification') {
          commonSendWebexAlert("base-config-java", "master")
      }
   }
}
