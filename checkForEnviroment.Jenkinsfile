#!groovy

def tmpDir = 'nexus'

if (params.DEPLOY_ENV != null && params.DEPLOY_ENV.equals('dev')){
        environment= 'dev-dc3'
} else if (params.DEPLOY_ENV != null && params.DEPLOY_ENV.equals('qa')){
        environment= 'qa-dc3'
} else if(params.DEPLOY_ENV != null && params.DEPLOY_ENV.equals('nr')){
        environment= 'nr-dc3'
} else if (params.DEPLOY_ENV != null && params.DEPLOY_ENV.equals('prod')){
        environment= 'prod-dc3'
} else if (params.DEPLOY_ENV != null && params.DEPLOY_ENV.equals('dr')){
        environment= 'dr-1ne'
}

node {
  if(fileExists("${tmpDir}")) {
      stage("Archiving Results")

      def tmpFile = "cassandra.txt.${env.BUILD_ID}"
      dir("${tmpDir}") {
          writeFile file: "${tmpFile}", text: 'example\n'
      }
  } else {
    stage('Create nexus dir') {
      sh "mkdir ${tmpDir}"
    }
  }

  stage('check the environment') {
    echo "The application will be deployed to :${params.DEPLOY_ENV}"

  }
}
