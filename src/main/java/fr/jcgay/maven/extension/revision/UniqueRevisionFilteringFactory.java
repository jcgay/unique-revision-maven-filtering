package fr.jcgay.maven.extension.revision;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.deployment.DeployRequest;
import org.eclipse.aether.impl.MetadataGenerator;
import org.eclipse.aether.impl.MetadataGeneratorFactory;
import org.eclipse.aether.installation.InstallRequest;

@Component(role = MetadataGeneratorFactory.class, hint = "revision-filtering", description = "Replace ${revision} by actual version in pom.xml")
public class UniqueRevisionFilteringFactory implements MetadataGeneratorFactory {

    @Requirement
    private ModelReader pomReader;

    @Requirement
    private ModelWriter pomWriter;

    @Requirement
    private Logger logger;

    @Override
    public MetadataGenerator newInstance(RepositorySystemSession repositorySystemSession, InstallRequest installRequest) {
        return new UniqueRevisionFiltering(pomReader, pomWriter, logger);
    }

    @Override
    public MetadataGenerator newInstance(RepositorySystemSession repositorySystemSession, DeployRequest deployRequest) {
        return new UniqueRevisionFiltering(pomReader, pomWriter, logger);
    }

    @Override
    public float getPriority() {
        return 20;
    }
}
