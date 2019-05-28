
// GET
def get = new URL("http://nexus.fuchicorp.com/service/rest/v1/components?repository=webplatform").openConnection();
def getRC = get.getResponseCode();
println(get.getInputStream().getText());
if(getRC.equals(200)) {
    println(get.getInputStream().getText());
}
