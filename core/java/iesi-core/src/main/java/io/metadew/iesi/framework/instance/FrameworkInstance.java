package io.metadew.iesi.framework.instance;

import io.metadew.iesi.framework.configuration.framework.FrameworkConfiguration;
import io.metadew.iesi.framework.configuration.metadata.repository.MetadataRepositoryConfiguration;
import io.metadew.iesi.framework.crypto.FrameworkCrypto;
import io.metadew.iesi.framework.definition.FrameworkInitializationFile;
import io.metadew.iesi.framework.execution.FrameworkControl;
import io.metadew.iesi.framework.execution.FrameworkExecution;
import io.metadew.iesi.framework.execution.FrameworkExecutionContext;
import io.metadew.iesi.metadata.repository.MetadataRepository;
import io.metadew.iesi.runtime.ExecutionRequestExecutorService;

public class FrameworkInstance {

    private static FrameworkInstance INSTANCE;

    public synchronized static FrameworkInstance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FrameworkInstance();
        }
        return INSTANCE;
    }

    private FrameworkInstance() {
    }


    public void init(FrameworkInitializationFile frameworkInitializationFile, FrameworkExecutionContext context) {
        init();
    }

    public void init() {
//        // Get the framework configuration
//        FrameworkConfiguration frameworkConfiguration = FrameworkConfiguration.getInstance();
//        frameworkConfiguration.init();
//
//        FrameworkCrypto.getInstance();
//
//        // Set appropriate initialization file
//        if (frameworkInitializationFile.getName().trim().isEmpty()) {
//            frameworkInitializationFile = new FrameworkInitializationFile(frameworkConfiguration.getFrameworkCode() + "-conf.ini");
//        }
//
//        // Prepare configuration and shared Metadata
//        FrameworkControl frameworkControl = FrameworkControl.getInstance();
//        frameworkControl.init(logonType, frameworkInitializationFile);
//
//        FrameworkActionTypeConfiguration.getInstance().setActionTypesFromPlugins(frameworkControl.getFrameworkPluginConfigurationList());
//        List<MetadataRepository> metadataRepositories = new ArrayList<>();
//
//        for (MetadataRepositoryConfiguration metadataRepositoryConfiguration : frameworkControl.getMetadataRepositoryConfigurations()) {
//            metadataRepositories.addAll(metadataRepositoryConfiguration.toMetadataRepositories());
//
//        }
//        MetadataRepositoryConfiguration.getInstance().init(metadataRepositories);
//
//        FrameworkExecution.getInstance().init(context);
//        // TODO: move Executor (Request to separate module)
//        ExecutionRequestExecutorService.getInstance();
    }

    public void shutdown() {
        for (MetadataRepository metadataRepository : MetadataRepositoryConfiguration.getInstance().getMetadataRepositories()) {
            if (metadataRepository != null) {
                metadataRepository.shutdown();
            }
        }
    }


}