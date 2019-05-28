
def branch = 'qa'
def repositoryName = 'webplatform'
switch(branch) {
    case 'master':
    repositoryName = repositoryName + '-prod'
    break

    case 'qa':
    repositoryName = repositoryName +  '-qa'
    break

    case 'dev':
    repositoryName = repositoryName + '-dev'
    break

    default:
        repositoryName = null
        print('You are using unsupported branch name')
  }

println(repositoryName)
