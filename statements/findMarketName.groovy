import java.net.URI;
import java.text.SimpleDateFormat
def params = [:]

String dateTime   = new SimpleDateFormat("yyyy/MM/dd.HH-mm-ss").format(Calendar.getInstance().getTime())
def BUILD_NUMBER = 2
def deployedToEnv = 'dev'
params['ConfigFile'] = 'qa/us-east-1/ipe16-us.tf'


tagForGit = "deploy_${deployedToEnv}_${BUILD_NUMBER}_${dateTime}"

if (params['ConfigFile'].split('/')[-1].contains('-')) {
  def market = params['ConfigFile'].split('/')[-1].split('-')[-1].split("\\.")[0];
  tagForGit = "deploy_${deployedToEnv}_${market}_${BUILD_NUMBER}_${dateTime}"
}



println(tagForGit)
