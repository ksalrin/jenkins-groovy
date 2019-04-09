import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils
import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.amazonaws.services.ec2.model.CreateTagsRequest
import com.amazonaws.services.ec2.model.Tag
import com.amazonaws.services.ec2.model.Image
import com.amazonaws.services.ec2.model.StartInstancesRequest
import com.amazonaws.services.ec2.model.StopInstancesRequest
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation
import com.amazonaws.services.ec2.model.DescribeImagesResult
import com.amazonaws.services.ec2.model.CreateImageRequest
import com.amazonaws.services.ec2.model.CreateImageResult
import com.amazonaws.services.ec2.waiters.AmazonEC2Waiters
import com.amazonaws.services.ec2.model.DeregisterImageRequest
import com.amazonaws.services.ec2.model.RebootInstancesRequest
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest
import com.amazonaws.services.ec2.model.InstanceStatus
import com.amazonaws.services.ec2.model.InstanceState
import com.amazonaws.services.ec2.model.RebootInstancesResult
import com.amazonaws.services.ec2.model.StopInstancesRequest
import com.amazonaws.services.ec2.model.StartInstancesRequest
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.DescribeInstancesResult
import com.amazonaws.services.ec2.model.Reservation
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.regions.Regions
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.BasicAWSCredentials
import java.text.SimpleDateFormat



class EC2Utils implements Serializable {

    public AmazonEC2 ec2client
    public String credentialProfileName
    public Regions region
    private static EC2Utils instance = null

    public static EC2Utils sharedInstance() {
        if (instance == null) {
            instance = new EC2Utils()
        }
        return instance
    }

    public void init(def profileName, def region) {
        this.credentialProfileName = profileName
        this.region = region
        this.ec2client = AmazonEC2ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider(profileName)).withRegion(region).build()
    }

    public String getPublicDNS(String instanceID) {
        DescribeInstancesRequest req = new DescribeInstancesRequest()
        req.withInstanceIds(["${instanceID}"] as String[])
        DescribeInstancesResult result = ec2client.describeInstances(req)
        def reservations = result.getReservations()
        if (reservations != null) {
            for (Reservation reservation : reservations) {
                def instances = reservation.getInstances()
                if (instances != null) {
                    for (Instance instance : instances) {
                        def id = instance.getInstanceId()
                        if (id == instanceID) {
                            return instance.getPublicDnsName()
                        }
                    }
                }
            }
        }
        return ""
    }

    public String getPrivateDNS(String instanceID) {
        DescribeInstancesRequest req = new DescribeInstancesRequest()
        req.withInstanceIds(["${instanceID}"] as String[])
        DescribeInstancesResult result = ec2client.describeInstances(req)
        def reservations = result.getReservations()
        if (reservations != null) {
            for (Reservation reservation : reservations) {
                def instances = reservation.getInstances()
                if (instances != null) {
                    for (Instance instance : instances) {
                        def id = instance.getInstanceId()
                        if (id == instanceID) {
                            return instance.getPrivateIpAddress()
                        }
                    }
                }
            }
        }
        return ""
    }

    public void stopInstance(String instanceID) {
        StopInstancesRequest stopReq = new StopInstancesRequest()
        stopReq.withInstanceIds(["${instanceID}"] as String[])
        ec2client.stopInstances(stopReq)
    }

    public void startInstance(String instanceID) {
        StartInstancesRequest startReq = new StartInstancesRequest()
        startReq.withInstanceIds(["${instanceID}"] as String[])
        ec2client.startInstances(startReq)
    }

    public String getInstanceState(String instanceID) {
        DescribeInstanceStatusResult instResult = getInstanceStatus(instanceID)
        def statuses = []
        statuses = instResult.getInstanceStatuses()
        if (statuses != null) {
            for (InstanceStatus instanceStatus : statuses) {
                String id = instanceStatus.getInstanceId()
                if (id == instanceID) {
                    return instanceStatus.getInstanceState().getName()
                }
            }
        }
        return ""
    }

    public DescribeInstanceStatusResult getInstanceStatus(String instanceID) {
        DescribeInstanceStatusRequest instReq = new DescribeInstanceStatusRequest()
        instReq.setIncludeAllInstances(true)
        instReq.withInstanceIds(["${instanceID}"] as String[])
        DescribeInstanceStatusResult instResult = ec2client.describeInstanceStatus(instReq)
        return instResult
    }

    public void updateAMIStatus(def amiid, def status) {
        def tags = []
        tags.add(new Tag("Status", "${status}"));
        def ids = [];
        ids.add(amiid);
        setAMITags(ids, tags)
    }

    public String createNewAmi(String instanceID, String amiName) {
        CreateImageRequest imgReq = new CreateImageRequest(instanceID, amiName)
        CreateImageResult imgResult = ec2client.createImage(imgReq)
        String newAmiID = ""
        try {
            newAmiID = imgResult.getImageId()
        } catch (ex) {
            println(ex)
        }

        return newAmiID
    }

    public String getAMIState(def imgID) {
        DescribeImagesRequest req = new DescribeImagesRequest()
        req.withImageIds(imgID)
        DescribeImagesResult results = ec2client.describeImages(req)
        Image img = results.getImages().last()
        String state = ""
        try {
            if (img != null) {
                state = img.getState()
            }
        } catch (ex) {
            println(ex)
        }

        return img.getState()
    }

    public void deleteOldAMIsWithFilters(def filters) {
        // Delete AMIs that have the same tags
        DescribeImagesRequest req = new DescribeImagesRequest()
        req.withFilters((Filter[]) filters)
        DescribeImagesResult results = ec2client.describeImages(req)
        for (Image img : results.getImages()) {
            String imgID = img.getImageId()
            Script.env.println "Deleting DEV AMI [${imgID}]"
            DeregisterImageRequest deregReq = new DeregisterImageRequest(imgID)
            ec2client.deregisterImage(deregReq)
        }
    }

    public String getLatestAmiIDWithFilters(def filters, def ownerID) {
        DescribeImagesRequest req = new DescribeImagesRequest()
        req.withOwners([ownerID] as String[]).withFilters((Filter[]) filters)
        DescribeImagesResult results = ec2client.describeImages(req)
        if (results.getImages() != null && results.getImages().size() > 0) {
            Image img = results.getImages().last()
            return img.getImageId();
        } else {
            return ""
        }
    }

    public Image getLatestAmiWithFilters(def filters, def ownerID) {
        DescribeImagesRequest req = new DescribeImagesRequest()
        req.withOwners([ownerID] as String[]).withFilters((Filter[]) filters)
        DescribeImagesResult results = ec2client.describeImages(req)
        if (results.getImages() != null && results.getImages().size() > 0) {
            Image img = results.getImages().last()
            return img
        } else {
            return null
        }
    }

    public Boolean tagExistInAMI(Image img, String tagKey, String tagValue) {
        for (Tag tag : img.getTags()) {
            if (tag.getKey() == tagKey && tag.getValue() == tagValue) {
                return true
            }
        }
        return false
    }

    public void setAMITags(def imageIDs, def tags) {
        CreateTagsRequest tagsRequest = new CreateTagsRequest(imageIDs, tags)
        ec2client.createTags(tagsRequest)
    }

    public String getInstanceStateByName(AmazonEC2 ec2client, String instanceName) {
        for (Reservation reservation : ec2client.describeInstances().reservations) {
            Instance instance = reservation.instances.find { findInstanceNameWithTags(it) == instanceName }
            if (instance) {
                return instance.state.name
            }
        }
        null
    }

    public String getInstancePublicDnsNameByName(AmazonEC2 ec2client, String instanceName) {
        for (Reservation reservation : ec2client.describeInstances().reservations) {
            Instance instance = reservation.instances.find { findInstanceNameWithTags(it) == instanceName }
            if (instance) {
                return instance.publicDnsName
            }
        }
        null
    }

    public void startInstanceByName(AmazonEC2 ec2client, String name) {
        for (Reservation reservation : ec2client.describeInstances().reservations) {
            Instance inst = reservation.instances.find {findInstanceNameWithTags(it) == name }
            if (inst) {
                ec2client.startInstances(new StartInstancesRequest([inst.instanceId]))
            }
        }
    }
    public void stopInstanceByName(AmazonEC2 ec2client, String name) {
        for (Reservation reservation : ec2client.describeInstances().reservations) {
            Instance inst = reservation.instances.find {findInstanceNameWithTags(it) == name }
            if (inst) {
                ec2client.stopInstances(new StopInstancesRequest([inst.instanceId]))
            }
        }
    }

    public String findInstanceNameByTags(Instance instance) {
        for (Tag tag : instance.tags) {
            if (tag.key.toLowerCase() == 'name') {
                return tag.value
            }
        }
        null
    }
}
