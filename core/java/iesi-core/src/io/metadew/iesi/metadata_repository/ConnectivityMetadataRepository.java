package io.metadew.iesi.metadata_repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.metadew.iesi.framework.execution.FrameworkExecution;
import io.metadew.iesi.metadata.configuration.ConnectionConfiguration;
import io.metadew.iesi.metadata.configuration.EnvironmentConfiguration;
import io.metadew.iesi.metadata.configuration.ImpersonationConfiguration;
import io.metadew.iesi.metadata.configuration.MetadataTableConfiguration;
import io.metadew.iesi.metadata.definition.*;
import io.metadew.iesi.metadata_repository.repository.Repository;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.text.MessageFormat;
import java.util.UUID;

public class ConnectivityMetadataRepository extends MetadataRepository {

    public ConnectivityMetadataRepository(String frameworkCode, String name, String scope, String instanceName, Repository repository, String repositoryObjectsPath,  String repositoryTablesPath) {
        super(frameworkCode, name, scope, instanceName, repository, repositoryObjectsPath, repositoryTablesPath);
    }

    @Override
    public String getDefinitionFileName() {
        return "ConnectivityTables.json";
    }

    @Override
    public String getObjectDefinitionFileName() {
        return "ConnectivityObjects.json";
    }

    @Override
    public String getCategory() {
        return "connectivity";
    }

    @Override
    public String getCategoryPrefix() {
        return "CXN";
    }

    @Override
    public void create(boolean generateDdl) {
        System.out.println("create");
    }

    @Override
    public void createMetadataRepository(File file, String archiveFolder, String errorFolder, UUID uuid) {
        System.out.println("create metadata repository");
    }

    @Override
    public void save(DataObject dataObject, FrameworkExecution frameworkExecution) {
        // TODO: based on MetadataRepository object decide to insert or not insert the objects
        // TODO: insert should be handled on database level as insert can differ from database type/dialect? JDBC Dialect/Spring
        System.out.println("save");
        System.out.println(dataObject.getType());
        ObjectMapper objectMapper = new ObjectMapper();
        if (dataObject.getType().equalsIgnoreCase("connection")) {
            Connection connection = objectMapper.convertValue(dataObject.getData(), Connection.class);
            ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(connection,
                    frameworkExecution);
            executeQuery(connectionConfiguration.getInsertStatement(), "writer");
        } else if (dataObject.getType().equalsIgnoreCase("environment")) {
            Environment environment = objectMapper.convertValue(dataObject.getData(), Environment.class);
            EnvironmentConfiguration environmentConfiguration = new EnvironmentConfiguration(environment,
                    frameworkExecution);
            executeQuery(environmentConfiguration.getInsertStatement(), "writer");
        } else if (dataObject.getType().equalsIgnoreCase("impersonation")) {
            Impersonation impersonation = objectMapper.convertValue(dataObject.getData(), Impersonation.class);
            ImpersonationConfiguration impersonationConfiguration = new ImpersonationConfiguration(impersonation, frameworkExecution);
            executeQuery(impersonationConfiguration.getInsertStatement(), "writer");
        } else if (dataObject.getType().equalsIgnoreCase("repository")) {
            // TODO
        } else 	{
            frameworkExecution.getFrameworkLog().log(MessageFormat.format("This repository is not responsible for loading saving {0}", dataObject.getType()), Level.WARN);
        }
    }
}
