#!/usr/bin/env groovy

// Check build
File pomInTarget = new File("$basedir/target/pom.xml")
def projectInTarget = new XmlSlurper().parse(pomInTarget)
assert projectInTarget.version == '1.0-SNAPSHOT'

// Check local repository
File pom = new File("$localRepositoryPath/fr/jcgay/maven/extension/urmf/replace-revision-in-pom/1.0-SNAPSHOT/replace-revision-in-pom-1.0-SNAPSHOT.pom")
def project = new XmlSlurper().parse(pom)
assert project.version == '1.0-SNAPSHOT'
assert project.dependencyManagement.dependencies[0].dependency.version == '1.0-SNAPSHOT'
assert project.dependencies[0].dependency.version == '1.0-SNAPSHOT'

return true
