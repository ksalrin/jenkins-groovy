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
