package com.mcd
// This pipeline is to deploy a feature to a dev environment
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials
import com.cloudbees.plugins.credentials.CredentialsProvider
import hudson.util.Secret

import java.net.URLConnection
import java.io.InputStream
import org.yaml.snakeyaml.Yaml
import java.text.SimpleDateFormat

def runPipeline(deployContainers = []) {

    def commonDeployer = new com.mcd.CommonDeployer()
    def consulLoader = new com.mcd.ConsulLoader()
    def liquibaseRun = new com.mcd.LiquibasePipeline()
    def openTest = new com.mcd.OpenTestDeploypipeline()
    def postmanTest = new com.mcd.PostmanDeploypipeline()
    def jmeterTest = new com.mcd.JMeterTestDeploypipeline()
    def deployData = []
    def k8slabel = "jenkins-pipeline-${UUID.randomUUID().toString()}"
    def jenkinsjoblabel = "${env.JOB_NAME}-${env.BUILD_NUMBER}"
    String dateTime = new SimpleDateFormat("yyyy/MM/dd.HH-mm-ss").format(Calendar.getInstance().getTime())
    def auroraLogin = [:]

    def deployedToEnv = ""
    def deployAccount = ""
    def gatingConfig = [:]
    def helmDebug = params["HelmDebug"]

    def jobName = "${JOB_BASE_NAME}"
        .toLowerCase()
        .replace(" ", "-")
        .replace(".net", "dotnet")
    jobName = jobName.substring(0, jobName.lastIndexOf("-deploy"))
    def envDeployment = params['ConfigFile'].split('/')[-1].replace('.tf', '')
    def tagForGit = "deploy_${envDeployment}_${dateTime}"
    def branchName = "${scm.branches[0].name}".replaceAll(/^\*\//, '')
    def gitCredId  = scm.getUserRemoteConfigs()[0].getCredentialsId()
    String repoUrl = "${scm.getUserRemoteConfigs()[0].getUrl()}".replace('https://Svc_VET_Stash@', '')
    println(repoUrl)
    println(branchName)
    println(jobName)


    def slavePodTemplate = """
    metadata:
      labels:
        k8s-label: ${k8slabel}
      annotations:
        jenkinsjoblabel: ${jenkinsjoblabel}
        iam.amazonaws.com/role: ${node_iam_role}
    spec:
      affinity:
        podAntiAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
          - labelSelector:
              matchExpressions:
              - key: component
                operator: In
                values:
                - jenkins-jenkins-master
            topologyKey: "kubernetes.io/hostname"
      containers:
    """
    def num_of_opentest_actor_web = 1
    def num_of_opentest_actor_api = 3
    def num_of_opentest_actor_mob = 1

    if ( deployContainers.size() > 0 ){
      deployContainers.each { container ->
        if ( container.count == 1 ) {
          slavePodTemplate = slavePodTemplate + specs(container.name)
        } else {
          container.count.times{
            //Appends container number to name
            tempcontainer = specs(container.name)
            containerSpec = tempcontainer.replace("- name: " + container.name, "- name: " + container.name + "-" + it)
            slavePodTemplate = slavePodTemplate + containerSpec
          }
        }
      }
    } else {

  /*
   * We want to create a for loop so we can generate templates for each of the opentest actor containers
   * This will create three containers of each actor in the same pod, at some point we could probably
   * make this more generic
   */

    String opentest_actor_web_tpl = """ """

    String opentest_actor_api_tpl = """ """

    String opentest_actor_mob_tpl = """ """

    num_of_opentest_actor_web.times {
      opentest_actor_web_tpl += """
      - name: opentest-actor-web-${it}
        image: vet-docker.docker.artifactory.vet-tools.digitalecp.mcd.com/dev-opentest-actor-web-container:latest
        imagePullPolicy: Always
        tty: true
        """
    }

    num_of_opentest_actor_api.times {
      opentest_actor_api_tpl += """
      - name: opentest-actor-api-${it}
        image: vet-docker.docker.artifactory.vet-tools.digitalecp.mcd.com/dev-opentest-actor-api-container:latest
        imagePullPolicy: Always
        tty: true
        """
    }

    num_of_opentest_actor_mob.times {
      opentest_actor_mob_tpl += """
      - name: opentest-actor-mob-${it}
        image: vet-docker.docker.artifactory.vet-tools.digitalecp.mcd.com/dev-opentest-actor-mob-container:latest
        imagePullPolicy: Always
        tty: true
        """
    }

    slavePodTemplate = slavePodTemplate + """
      - name: opentest-server
        image: vet-docker.docker.artifactory.vet-tools.digitalecp.mcd.com/dev-opentest-server-container:latest
        imagePullPolicy: Always
        ports:
        - containerPort: 3000
        tty: true
      - name: postman
        image: postman/newman_ubuntu1404
        imagePullPolicy: Always
        tty: true
        command:
        - cat
      ${opentest_actor_web_tpl}
      ${opentest_actor_api_tpl}
      ${opentest_actor_mob_tpl}
      - name: liquibase
        image: vet-docker.docker.artifactory.vet-tools.digitalecp.mcd.com/docker-liquibase-image:master-latest
        imagePullPolicy: Always
        tty: true
        command:
        - cat
      - name: jmeter
        image: justb4/jmeter
        imagePullPolicy: Always
        command:
        - cat
        tty: true
      - name: docker
        image: docker:latest
        imagePullPolicy: Always
        resources:
          requests:
            cpu: 100m
            memory: 128Mi
        command:
        - cat
        tty: true
        volumeMounts:
          - mountPath: /var/run/docker.sock
            name: docker-sock
      - name: vetbuildtools
        image: vet-docker.docker.artifactory.vet-tools.digitalecp.mcd.com/docker-build-tools-image:master-latest
        imagePullPolicy: Always
        resources:
          requests:
            cpu: 100m
            memory: 128Mi
        command:
        - cat
        tty: true"""
  }
    slavePodTemplate = slavePodTemplate + """
      securityContext:
        runAsUser: 0
        fsGroup: 0
      volumes:
        - name: docker-sock
          hostPath:
            path: /var/run/docker.sock
    """

    println "K8SLabel: ${k8slabel}"

    node('master') {
      wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
        sh """#!/usr/bin/env bash
          set +x
          RED='\033[0;31m'
          BOLD='\033[1m'
          NC='\033[0m'
          COMMONLIB_HEAD="\$(cat \"${workspace}@libs/CommonLib/.git/HEAD\")"
          COMMONLIB_BRANCHNAME="\$(cat \"${workspace}@libs/CommonLib/.git/FETCH_HEAD\" | grep \"\$COMMONLIB_HEAD\" | cut -d \"'\" -f 2)"
          echo "+--------------------------- Pre-check ---------------------------+"
          echo "You are using CommonLib on branch: \$COMMONLIB_BRANCHNAME"
          if [[ "\$COMMONLIB_BRANCHNAME" != *"master"* ]] && [[ "\$COMMONLIB_BRANCHNAME" != *"release/v"* ]]; then
            echo "\${RED}You are running a version of CommonLib that is \${BOLD}NOT supported.\${NC}"
            echo "\${RED}Please use \${BOLD}CommonLib@master\${NC}\${RED} or a supported \${NC}\${RED}release branch\${NC}\${RED}.\${NC}"
          fi
          echo "+-----------------------------------------------------------------+"
        """
      }
    }

    podTemplate(name: k8slabel, label: k8slabel, yaml: slavePodTemplate) {
    node (k8slabel) {
        currentBuild.result = 'SUCCESS'
        withCredentials([usernamePassword(credentialsId: 'ARTIFACTORY_ACCESS', passwordVariable: 'password', usernameVariable: 'username')]) {
          artifactoryServer = Artifactory.newServer(url: "${artifactory_host_url}", username: username, password: password)
        }
        try {
            stage ("Deployment Pull") {
                // Get deployment package
                deploymentArtifactPath = commonDeployer.downloadDeploymentArtifact(jobName)
                deployData = readYaml file: "deployment.yaml"
                deployData.deploymentArtifactPath = deploymentArtifactPath
                println deployData
            }

            // We are not doing true parallel due to limitations in visualizaions:
            // see https://issues.jenkins-ci.org/browse/JENKINS-38442

            // Pre build tasks
            stage ("Artifact Pull") {
                parallel commonDeployer.generateSteps("Artifact Pull", deployData) {
                  if (it.buildtype == "lambda") {
                    commonDeployer.lambda_artifact_pull(it, deployData)
                  }
                  else if (it.buildtype == "muleonprem") {
                    commonDeployer.muleonprem_artifact_pull(it, deployData, params["ConfigFile"])
                  }
                  else if (it.buildtype == "mulecloudhub") {
                    commonDeployer.mulecloudhub_artifact_pull(it, deployData, params["ConfigFile"])
                  }
                  else {
                    commonDeployer.docker_artifact_pull(it, deployData)
                  }
                }

            }

            // Deployment Scan
            stage ("Deployment Scan") {
              if (deployData.deployments) { // New style
                deployData.deployments.each { k, v ->
                  commonDeployer.terraform_scan("${workspace}/${v.path}")
                }
              } else { // Old style
                commonDeployer.terraform_scan("${workspace}/${deployData.terraform_path}")
              }
              // Test SQL (only if there is Liquibase in the deployment)
              if (deployData.liquibase_path && fileExists("${workspace}/${deployData.liquibase_path}")) {
                auroraLogin = liquibaseRun.liquibase_aurora_login(deployData.liquibase_path, params["ConfigFile"])
                liquibaseRun.liquibase_test(auroraLogin, deployData.liquibase_path)
              }
            }

            // Plan or Apply
            stage ("Plan / Apply") {
              configFilePath =""
              if (deployData.deployments) { // New style
                deployData.deployments.each { k, v ->
                  commonDeployer.terraform_apply(
                    params["ApplyChanges"],
                    "${workspace}/${v.path}",
                    params["ConfigFile"],
                    jobName,
                    deployData.git_hash.take(6),
                    (deployData.components.collect {
                      cur -> return cur.name
                    }).join(","), // Create a comma seperated string of build names
                    (deployData.components.collect {
                      cur -> return (cur.buildtype == "docker") ? cur.build_artifact.substring(cur.build_artifact.indexOf("/") + 1) : cur.build_artifact
                    }).join(","), // Create a comma seperated string of build artifacts
                    deployData.version,
                    helmDebug
                  )
                }
                // grab the path for the first tf config file
                configFilePath = "${workspace}/${deployData.deployments.take(1).values()[0].path}/configurations/${params["ConfigFile"]}"
              } else if (deployData.terraform_path) { // Old style
                commonDeployer.terraform_apply(
                  params["ApplyChanges"],
                  "${workspace}/${deployData.terraform_path}",
                  params["ConfigFile"],
                  jobName,
                  deployData.git_hash.take(6),
                  (deployData.components.collect {
                    cur -> return cur.name
                  }).join(","), // Create a comma seperated string of build names
                  (deployData.components.collect {
                    cur -> return (cur.buildtype == "docker") ? cur.build_artifact.substring(cur.build_artifact.indexOf("/") + 1) : cur.build_artifact
                  }).join(","), // Create a comma seperated string of build artifacts
                  deployData.version,
                  helmDebug
                )
                configFilePath = "${workspace}/${deployData.terraform_path}/configurations/${params["ConfigFile"]}"
              }
              deployedToEnv = commonDeployer.getTFVariableValue(configFilePath, "environment")
              deployAccount = commonDeployer.getTFVariableValue(configFilePath, "deploy_account")
            }

            // Apply SQL (only show stage if there is Liquibase in the deployment)
            if (deployData.liquibase_path && fileExists("${workspace}/${deployData.liquibase_path}")) {
              stage ("Liquibase Run") {
                if (params["ApplyChanges"]) {
                  liquibaseRun.liquibase_run(auroraLogin, deployData.liquibase_path)
                }
              }
            }

            stage ("Gating Config Pull") {
              gatingConfig = consulLoader.pullGateConfigsFromConsul(deployedToEnv)
              println("Gating configuration retrieved:")
              println(gatingConfig)
            }

            // Run OpenTest
            stage ("OpenTest Run") {
              if (deployData.opentest_path) {
                openTest.opentest_run(deployData.opentest_path, jobName, num_of_opentest_actor_api, deployedToEnv, deployAccount, gatingConfig.opentest)
              } else {
                println("No OpenTest data. OpenTest was not run.")
              }
            }

            // Run Postman
            stage ("Postman Test Run") {
              if (deployData.postman_path) {
                postmanTest.postmantest_run(deployData.postman_path, gatingConfig.postman)
              } else {
                println("No Postman test data. Postman was not run.")
              }
            }

            // Run Jmeter
            stage ("Jmeter Test Run") {
              if (deployData.jmeter_path) {
                jmeterTest.jmetertest_run(deployData.jmeter_path, deployedToEnv, gatingConfig.jmeter)
              } else {
                println("No Jmeter test data. Jmeter was not run.")
              }
            }


            stage ("Tag Deployment Artifact"){
              if(params["ApplyChanges"]){
                 def commonEmail = new com.mcd.CommonEmail()
                newProperties = [
                  'deployedToAWSEnv': [params["ConfigFile"].split("/")[0]],
                  'deployjob.number': [env.BUILD_NUMBER],
                  'deployjob.name': [env.JOB_NAME],
                  'commit': [deployData.git_hash],
                  'deployedBy': [commonEmail.getJobSubmitter()]
                ]

                currentProperties = commonDeployer.getArtifactProperties(deployData.deploymentArtifactPath)
                currentProperties.each { key, value ->
                  if (newProperties[key]) {
                    newProperties[key] = newProperties[key].toSet()
                    newProperties[key].addAll(value)
                  }
                }
                commonDeployer.putArtifactProperties(deployData.deploymentArtifactPath,newProperties)
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: "${gitCredId}", usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
                  checkout scm
                  sh("""
                  git config --global user.email '${GIT_USERNAME}'
                  git config --global user.name  '${GIT_USERNAME}'
                  git config --global credential.helper cache
                  mkdir '${WORKSPACE}/git_tagger'
                  git clone -b ${branchName} https://${GIT_USERNAME}:${GIT_PASSWORD}${repoUrl} ${WORKSPACE}/git_tagger """)
                  dir("${WORKSPACE}/git_tagger") {
                    sh("""git tag -a '${tagForGit}' -m 'Jenkins deployment has been deployed successfully.
                    git push https://${GIT_USERNAME}:${GIT_PASSWORD}${repoUrl} --tags """)
                  }
               }
              } else {
                println("Artifact not tagged because ApplyChanges is not set")
              }
            }

        } catch (e) {
            if (e instanceof org.jenkinsci.plugins.scriptsecurity.sandbox.RejectedAccessException) {
                throw e
            }
            currentBuild.result = 'FAILURE'
            println "ERROR Detected:"
            println e.getMessage()
            def sw = new StringWriter()
            def pw = new PrintWriter(sw)
            e.printStackTrace(pw)
            println sw.toString()
        } finally {

            def commonEmail = new com.mcd.CommonEmail()
            commonEmail.sendDeployToJobSubmitter()
			// Check if there is an additional email list specified in build.yaml and send email if present (VET-652)

		    if (!deployData.emaildistributionlist) {
		        // do nothing as no list found in build.yaml
		    }
		    else {
		        def additionalEmail = new com.mcd.CommonEmail()
		        additionalEmail.sendDeployToDistList()
		    }

        }
    } // node (k8slabel)
    } // podTemplate

}

return this
