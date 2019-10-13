
def envNum = '607546651489'

def awsIam = "arn:aws:iam::{ENV_NUM}:role/JenkinsSlavePodsRole".replace('{ENV_NUM}', envNum);

println(awsIam)
