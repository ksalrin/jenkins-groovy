

/*
Function to run jenkins base job reguraly.
<params> Name of the job
*/

def runBaseJobs(String baseJob, String jobName) {
  if (baseJob.contains('base')) {
    if (jobName == 'master' || jobName == 'develop' ) {
      println('Job contains base master/develop')
    }
  }
}

def scheduleBaseJobs(String baseJob, String jobName) {
  if (baseJob.contains('base')) {
    if (jobName == 'master' || jobName == 'develop') {
      println('Job contains base master/develop')
    }
}

// runBaseJobs('base_job_name', 'develop')
scheduleBaseJobs('base_job_name', 'develop')
scheduleBaseJobs('base_job_name', 'master')
