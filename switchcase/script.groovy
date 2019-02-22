def builddata = [:]

builddata['tag'] = []
newProperties = [
 'debug': ("testing/VET-1557" != "master") ? ["true"] : ["false"],
 'release': ("testing/VET-1557" == "master") ? ["true"] : ["false"]
]

if (newProperties['debug']) {
  switch (newProperties['debug'][0]) {
      case 'true':
          if (newProperties['release'][0] == 'false'){
            builddata['tag'].add('qa')
            builddata['tag'].add('dev')
            break
          }
      case 'false':
          if (newProperties['release'][0] == 'true'){
            builddata['tag'].add('stage')
            builddata['tag'].add('qa')
            builddata['tag'].add('prod')
            break
          }
      default:
          println('Tag should exist on builddata')
  }
} else {
  println('debug is does not exist please add debug')
}


if ( 'stage' in builddata['tag']) {
  println('This will go to prod')
}


if ( 'dev' in builddata['tag']) {
  println('This will go to qa')
}
