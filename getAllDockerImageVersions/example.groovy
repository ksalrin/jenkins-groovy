

node('master') {
    properties([ parameters([
      choice(name: 'SelectedDockerImage',
      choices: findDockerImages('dev'),
      description: 'Please select docker image to deploy!')
      ])])
}




println(example('farkhod'))
