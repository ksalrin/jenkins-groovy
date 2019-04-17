import groovy.util.XmlParser
import groovy.util.XmlSlurper


def text = '''
    <list>
        <technology>
            <name>Groovy</name>
            <test>Groovy</test>
        </technology>

    </list>
'''

def list = new XmlSlurper().parseText(text)

assert list instanceof groovy.util.slurpersupport.GPathResult
assert list.technology.name == 'Groovy'

println(list)
println(list.technology.test)
