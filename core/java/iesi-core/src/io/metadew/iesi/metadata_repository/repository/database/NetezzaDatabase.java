package io.metadew.iesi.metadata_repository.repository.database;

import io.metadew.iesi.framework.execution.FrameworkLog;
import io.metadew.iesi.metadata.definition.MetadataTable;
import io.metadew.iesi.metadata_repository.repository.database.connection.NetezzaDatabaseConnection;
import org.apache.logging.log4j.Level;

import java.text.MessageFormat;
import java.util.Optional;

public class NetezzaDatabase extends Database{

    String schema;


    public NetezzaDatabase(NetezzaDatabaseConnection databaseConnection, String schema) {
        super(databaseConnection);
        this.schema = schema;
    }

    @Override
    public String getSystemTimestampExpression() {
        return "CURRENT_TIMESTAMP";
    }

    @Override
    public String getAllTablesQuery(String pattern) {
        return "select SCHEMA as \"OWNER\", TABLENAME as \"TABLE_NAME\" from _V_TABLE where OWNER = '"
                + schema
                + "' and TABLENAME like '"
                + pattern
                + "%' order by TABLENAME asc";
    }

    @Override
    public String getCreateStatement(MetadataTable table, String tableNamePrefix) {
        return null;
    }


    @Override
    public void cleanTable(String tableName, FrameworkLog frameworkLog) {
        frameworkLog.log(MessageFormat.format("metadata.clean.table={0}", getSchema().map(schema -> schema + "." + tableName).orElse(tableName)), Level.INFO);
        String query = getSchema().map(schema -> "delete from " + schema + "." + tableName).orElse("delete from " + tableName);
        databaseConnection.executeQuery(query);
    }

    @Override
    public void dropTable(String tableName, FrameworkLog frameworkLog) {
        frameworkLog.log(MessageFormat.format("metadata.drop.table={0}", getSchema().map(schema -> schema + "." + tableName).orElse(tableName)), Level.INFO);
        String query = getSchema().map(schema -> "drop table " + schema + "." + tableName).orElse("drop table " + tableName);
        databaseConnection.executeQuery(query);
    }

    public Optional<String> getSchema() {
        return Optional.ofNullable(schema);
    }

    @Override
    String getCleanStatement(MetadataTable metadataTable, String tableNamePrefix) {
        return getSchema().map(schema -> "delete from " + schema + "." + tableNamePrefix + metadataTable.getName()).orElse("delete from " + tableNamePrefix + metadataTable.getName());
    }

    @Override
    public String getDropStatement(MetadataTable table, String tableNamePrefix) {
        return getSchema().map(schema -> "drop table " + schema + "." + tableNamePrefix + table.getName()).orElse("drop table " + tableNamePrefix + table.getName());
    }

}
