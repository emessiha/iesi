package io.metadew.iesi.connection.database;

import io.metadew.iesi.connection.database.connection.oracle.OracleDatabaseConnection;
import io.metadew.iesi.metadata.definition.MetadataField;
import io.metadew.iesi.metadata.definition.MetadataTable;

import java.util.List;
import java.util.stream.Collectors;

public class OracleDatabase extends SchemaDatabase {

    public OracleDatabase(OracleDatabaseConnection databaseConnection, String schema) {
        super(databaseConnection, schema);
    }
    public OracleDatabase(OracleDatabaseConnection databaseConnection, int initialPoolSize, int maximalPoolSize, String schema) {
        super(databaseConnection, initialPoolSize, maximalPoolSize, schema);
    }

    @Override
    public String getSystemTimestampExpression() {
        return "systimestamp";
    }

    @Override
    public String getAllTablesQuery(String pattern) {
        // pattern = tableNamePrefix + categoryPrefix
        return "select OWNER, TABLE_NAME from ALL_TABLES where"
                + getSchema().map(schema -> " owner = '" + schema + "' and").orElse("")
                + " TABLE_NAME like '"
                + pattern
                + "%' order by TABLE_NAME ASC";
    }

    public boolean addComments() {
        return true;
    }

    public String createQueryExtras() {
        return "\nLOGGING\nNOCOMPRESS\nNOCACHE\nNOPARALLEL\nMONITORING";
    }

    public String toQueryString(MetadataField field) {
        StringBuilder fieldQuery = new StringBuilder();
        // Data Types
        switch (field.getType()) {
            case "string":
                fieldQuery.append("VARCHAR2 (").append(field.getLength()).append(" CHAR)");
                break;
            case "flag":
                fieldQuery.append("CHAR (").append(field.getLength()).append(" CHAR)");
                break;
            case "number":
                fieldQuery.append("NUMBER");
                break;
            case "timestamp":
                fieldQuery.append("TIMESTAMP (6)");
                break;
        }

        // Default DtTimestamp
        if (field.getDefaultTimestamp().trim().equalsIgnoreCase("y")) {
            fieldQuery.append(" DEFAULT systimestamp");
        }

        // Nullable
        if (field.getNullable().trim().equalsIgnoreCase("n")) {
            fieldQuery.append(" NOT NULL");
        }
        return fieldQuery.toString();
    }

    @Override
    public String toPrimaryKeyConstraint(MetadataTable metadataTable, List<MetadataField> primaryKeyMetadataFields) {
        return "CONSTRAINT pk_" + metadataTable.getName() + " PRIMARY KEY (" + primaryKeyMetadataFields.stream().map(MetadataField::getName).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public String toFieldName(MetadataField field) {
        StringBuilder result = new StringBuilder();
        result.append(field.getName());
        return result.toString();
    }
}
