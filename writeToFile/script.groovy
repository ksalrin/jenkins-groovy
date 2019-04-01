def file = new File('writeToFile/example.txt')

def branch = 'dev'

def repository = 'webplatform-dev:0.2'


file.write "This is new text ${branch}."
