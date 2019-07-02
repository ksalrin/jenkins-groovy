
def baseName = 'base-master'
def jobName  = 'master'

if (baseName.contains('base'))  {
   if (jobName == 'master' || jobName == 'develop') {
     println('Condition is working')
   }
 }
