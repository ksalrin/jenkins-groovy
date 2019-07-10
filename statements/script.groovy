//
//
// /*
// Function to run jenkins base job reguraly.
// <params> Name of the job
// */
//
// def runBaseJobs(String baseJob, String jobName) {
//   if (baseJob.contains('base')) {
//     if (jobName == 'master' || jobName == 'develop' ) {
//       println('Job contains base master/develop')
//     }
//   }
// }
//
// def scheduleBaseJobs(String baseJob, String jobName) {
//   if (baseJob.contains('base')) {
//     if (jobName == 'master' || jobName == 'develop') {
//       println('Job contains base master/develop')
//     }
// }
//
// // runBaseJobs('base_job_name', 'develop')
// scheduleBaseJobs('base_job_name', 'develop')
// scheduleBaseJobs('base_job_name', 'master')

def baseName = 'example-job'
def JOB_URL = 'https://jenkins.fuchicorp.com/env-vars.html/'
def BUILD_URL = 'https://jenkins.fuchicorp.com/env-vars.html/'
def BUILD_NUMBER = 2
def GIT_URL = 'https://jenkins.fuchicorp.com/env-vars.html/'
def GIT_BRANCH = 'master'
def GIT_COMMIT = 'awdafafgre4tr32regt4t32ra4egrsa4re'


def body= """{
           "name": ${baseName},
           "url": "${JOB_URL}",
           "build": {
               "full_url": "${BUILD_URL}",
               "number": "${BUILD_NUMBER}",
               "phase": "COMPLETED",
               "status": "FAILURE",
               "url": "job/asgard/18/",
               "scm": {
                   "url": "${GIT_URL}",
                   "branch": "${GIT_BRANCH}",
                   "commit": "${GIT_COMMIT}"
               }
           }
       }"""
println(body)
