properties( [ parameters([
     choice( name: 'SelectedDockerImage', choices: ['one', 'two', 'three'], description: 'Please select docker image to deploy!'),
     booleanParam( defaultValue: false, description: 'Apply All Changes', name: 'terraformApply'),
     string( defaultValue: 'webplatform', name: 'mysql_database', value: 'dbwebplatform', description: 'Please enter database name')
     ]
     )] )

def defaultValueApp = 'webplatform'


println(params.mysql_database.class)
if (params.mysql_database != params.defaultValue) {
  echo "It did not works"

} else {
  echo "it did work"
}
