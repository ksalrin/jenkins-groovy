def environment = "qa-feature/cramis"

def devEnvs = ["dev", "int", "dvc"]
def qaEnvs = ["ipe", "stag", "stg", "mte", "pref"]

def isDev = devEnvs.any {
    // Any loops returns always bool 
    devEnv -> environment.startsWith(devEnv)
}

println(isDev)
