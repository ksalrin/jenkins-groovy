// tmpBuilddata = ''
// tmpBuilddata = readYaml file: "collectEntries/build.yaml"
//
//
// println(tmpBuilddata)


def appList = ["DevOpsApplication", "01.01.01", "awdawd", 123]
def appMap = [appList].collectEntries() // XXX

println(appList)
println(appMap)
println(appMap.DevOpsApplication)
