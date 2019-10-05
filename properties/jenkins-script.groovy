def String getEnvNum(environment){
  def commonBuildProperty = readProperties text: libraryResource('properties/common-library-build.properties')
  // switch (environment) {
  //   case 'DEV':
  //     return commonBuildProperty['DEV_NUM']
  //   case 'QA':
  //     return commonBuildProperty['QA_NUM']
  //   case 'CORP':
  //     return commonBuildProperty['CORP_NUM']
  //   case 'STAGE':
  //     return commonBuildProperty['STAGE_NUM']
  //   case 'PROD':
  //     return commonBuildProperty['PROD_NUM']
  //   default: return ""
  // }
  return commonBuildProperty
}



println(getEnvNum("DEV"))
