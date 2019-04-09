import groovy.json.JsonSlurper
data = """
{
    "Images": [
        {
            "VirtualizationType": "hvm",
            "Name": "ECP-3.4-RabbitMQ-Main-v101",
            "Hypervisor": "xen",
            "SriovNetSupport": "simple",
            "ImageId": "ami-0a271d8a38b17cab0",
            "StateReason": {
                "Message": "Client.InstanceTerminated:User has terminated or rebooted the instance",
                "Code": "Client.InstanceTerminated:User has terminated or rebooted the instance"
            },
            "State": "failed",
            "BlockDeviceMappings": [],
            "Architecture": "x86_64",
            "ImageLocation": "068560032983/ECP-3.4-RabbitMQ-Main-v101",
            "RootDeviceType": "ebs",
            "OwnerId": "068560032983",
            "CreationDate": "2019-04-08T18:09:37.000Z",
            "Public": false,
            "ImageType": "machine"
        }
    ]
}
"""

def myJsonreader = new JsonSlurper()

def data = myJsonreader.parseText(data).Images.State[0]
println(data)
