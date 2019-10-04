#!/usr/bin/env groovy
import groovy.json.JsonSlurper

def jsonReader = new JsonSlurper()
def dataFromUrl = jsonReader.parse( new URL('http://nexus.fuchicorp.com/service/rest/v1/components?repository=fscoding') )

def versions = dataFromUrl.items.version

def newVersion = 0.8


if (dataFromUrl.items.version.contains(newVersion)) {
  println('I am not going to deploy')
} else {
  println("I will deploy the code ")
}
