properties( [ parameters([
     choice( name: 'SelectedDockerImage', choices: ['one', 'two', 'three'], description: 'Please select docker image to deploy!'),
     booleanParam( defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'),
     string( defaultValue: 'webplatform', name: 'mysql_database', value: 'dbwebplatform', description: 'Please enter database name')
     ]
     )] )


stage('Checking for parameters') {
  echo "This is choice parameters: ${params.SelectedDockerImage}"
  echo "This is boolean parameters: ${params.terraformApply}"
  echo "This is string parameters: ${params.mysql_database}"
}



properties( [ parameters([
     choice( name: 'SelectedDockerImage', choices: ['one', 'two', 'three'], description: 'Please select docker image to deploy!'),
     booleanParam( defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'),
     string( defaultValue: 'webplatform', name: 'mysql_database', value: 'dbwebplatform', description: 'Please enter database name')
     ]
     )] )


node{
    stage('Test') {
        properties.each() {
          println(it)
        }
    }
}



def scheduleBaseJobs(String baseName, String jobName) {
  if (baseName.contains('base') ) {
    if (jobName == 'master' || jobName == 'develop') {
      properties([[$class: 'RebuildSettings',
      autoRebuild: false,
      rebuildDisabled: false],
      pipelineTriggers([cron('0 1-6 * * *')])])
    }
  }
}
