@Grab('org.yaml:snakeyaml:1.17')

import org.yaml.snakeyaml.Yaml

Yaml parser = new Yaml()
def  example = parser.load(("workingWithYaml/non-prod-WORKS.yaml" as File).text)


println(example.Account)
