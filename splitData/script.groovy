def mydata = '/dev/us-east-1/dev1.tf'

mydata = mydata.split('/')[-1].replace('.tf', '')
println(mydata)
