
// This script exmplains how to read properties file and use in scipts
Properties properties = new Properties()
File propertiesFile = new File('properties/common-build.properties')

propertiesFile.withInputStream {
    properties.load(it)
}


println(properties['packerArtifactPath'])
