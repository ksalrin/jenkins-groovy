def googleDomainName        = "kelly.com"


def fuchicorpJenkinsAdmin   = ['fsadykov', 'farrukh', 'ikambarov']
def commonJenkinsAdmin      = ['kelly', 'ali', 'berkayh']
def jenkinsAdmin = []


if (googleDomainName == 'fuchicorp.com') {
    fuchicorpJenkinsAdmin.each() {
        jenkinsAdmin.add(it)
    }
} else {
    commonJenkinsAdmin.each() {
        jenkinsAdmin.add(it)
    }
}




println("list of jenkins admins {$jenkinsAdmin}" )
