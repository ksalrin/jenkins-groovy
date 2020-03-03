
def Environment = 'uk'
def market = ''
def convertedEnvironment = Environment.split('-').first().toLowerCase()

if ( convertedEnvironment == "perf01") { 
    market = "us" 
} else if (convertedEnvironment == "perf02") { 
    market = "us" 
} else {
    market = Environment.split('-').last().toLowerCase()
}

def environmentMap = [
    'us':['us-east-1'], 
    'au':['ap-southeast-1'], 
    'eu':['eu-central-1'], 
    'de':['eu-central-1'], 
    'uk':['eu-central-1']]

return environmentMap[market]