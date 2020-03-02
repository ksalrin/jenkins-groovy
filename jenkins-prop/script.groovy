

def Environment = 'uk'


def get_region(env) {
    def market = ''
    def convertedEnvironment = env.split('-').first().toLowerCase()
    
    if ( convertedEnvironment == "perf01") { 
        market = "us" 
    } else if (convertedEnvironment == "perf02") { 
        market = "us" 
    } else {
        market = env.split('-').last().toLowerCase()
    }

    def environmentMap = [
        'us':['us-east-1'], 
        'au':['ap-southeast-1'], 
        'eu':['eu-central-1'], 
        'de':['eu-central-1'], 
        'uk':['eu-central-1']]

    return environmentMap[market]
}


println("Deploying to uk: " + get_region("uk"))
println("Deploying to us: " + get_region("us"))
println("Deploying to de: " + get_region("de"))