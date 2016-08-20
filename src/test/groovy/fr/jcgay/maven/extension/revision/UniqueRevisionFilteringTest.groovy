package fr.jcgay.maven.extension.revision

import org.apache.maven.model.io.DefaultModelReader
import org.apache.maven.model.io.DefaultModelWriter
import org.apache.maven.model.io.ModelReader
import org.apache.maven.model.io.ModelWriter
import org.codehaus.plexus.logging.Logger
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.util.artifact.SubArtifact
import spock.lang.Specification

class UniqueRevisionFilteringTest extends Specification {

    ModelReader pomReader = new DefaultModelReader()
    ModelWriter pomWriter = new DefaultModelWriter()
    Logger logger = Mock()

    UniqueRevisionFiltering uniqueRevision = new UniqueRevisionFiltering(pomReader, pomWriter, logger)

    File pom = File.createTempFile('pom', '.xml')

    def setup() {
        pom.deleteOnExit()
    }

    def 'artifact which is not a POM is left untouched'() {
        given:
        def artifact = new DefaultArtifact('fr.jcgay.test', 'jar-id', 'jar', '1.0')

        when:
        def result = uniqueRevision.transformArtifact(artifact)

        then:
        result == artifact
    }

    def 'replace ${revision} with current artifact version'() {
        given:
        pom << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.github.jcgay.example.version</groupId>
        <artifactId>parent-pom</artifactId>
        <version>\${revision}</version>
    </parent>

    <artifactId>submodule-1</artifactId>
    <version>\${revision}</version>
</project>
        """
        def artifact = new SubArtifact(new DefaultArtifact('fr.jcgay.test', 'jar-id', 'jar', '1.0-SNAPSHOT'), null, 'pom', pom)

        when:
        def result = uniqueRevision.transformArtifact(artifact)

        then:
        def project = new XmlSlurper().parse(result.file)
        project.parent.version == '1.0-SNAPSHOT'
        project.version == '1.0-SNAPSHOT'
    }

    def 'replace ${revision} with current artifact version without parent pom'() {
        given:
        pom << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <artifactId>submodule-1</artifactId>
    <version>\${revision}</version>
</project>
        """
        def artifact = new SubArtifact(new DefaultArtifact('fr.jcgay.test', 'jar-id', 'jar', '1.0-SNAPSHOT'), null, 'pom', pom)

        when:
        def result = uniqueRevision.transformArtifact(artifact)

        then:
        def project = new XmlSlurper().parse(result.file)
        project.version == '1.0-SNAPSHOT'
    }

    def 'left version untouched when it is not set to ${revision}'() {
        given:
        pom << """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.github.jcgay.example.version</groupId>
        <artifactId>parent-pom</artifactId>
        <version>dumb</version>
    </parent>

    <artifactId>submodule-1</artifactId>
    <version>dumb</version>
</project>
        """
        def artifact = new SubArtifact(new DefaultArtifact('fr.jcgay.test', 'jar-id', 'jar', '1.0-SNAPSHOT'), null, 'pom', pom)

        when:
        def result = uniqueRevision.transformArtifact(artifact)

        then:
        def project = new XmlSlurper().parse(result.file)
        project.parent.version == 'dumb'
        project.version == 'dumb'
    }
}
