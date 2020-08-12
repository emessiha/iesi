//package io.metadew.iesi.server.rest.executionrequest.dto;
//
//import io.metadew.iesi.common.configuration.metadata.repository.MetadataRepositoryConfiguration;
//import io.metadew.iesi.common.configuration.metadata.tables.MetadataTablesConfiguration;
//import io.metadew.iesi.connection.tools.SQLTools;
//import io.metadew.iesi.metadata.configuration.execution.ExecutionRequestLabelConfiguration;
//import io.metadew.iesi.metadata.configuration.execution.script.ScriptExecutionRequestConfiguration;
//import io.metadew.iesi.metadata.definition.execution.AuthenticatedExecutionRequest;
//import io.metadew.iesi.metadata.definition.execution.ExecutionRequest;
//import io.metadew.iesi.metadata.definition.execution.ExecutionRequestStatus;
//import io.metadew.iesi.metadata.definition.execution.NonAuthenticatedExecutionRequest;
//import io.metadew.iesi.metadata.definition.execution.key.ExecutionRequestKey;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.rowset.CachedRowSet;
//import java.sql.SQLException;
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Log4j2
//@Repository
//public class ExecutionRequestDtoRepository implements IExecutionRequestDtoRepository {
//
//    private final MetadataRepositoryConfiguration metadataRepositoryConfiguration;
//
//    @Autowired
//    public ExecutionRequestDtoRepository(MetadataRepositoryConfiguration metadataRepositoryConfiguration) {
//        this.metadataRepositoryConfiguration = metadataRepositoryConfiguration;
//    }
//
//    public int getTotalPages(int limit, String filterColumn, String searchParam, String request_from, String request_to) {
//        try {
//            String query = "SELECT " +
//                    "AUTH_EXECUTION_REQUEST.REQUEST_ID AS AUTH, " +
//                    "NON_AUTH_EXECUTION_REQUEST.REQUEST_ID AS NON_AUTH, " +
//                    "SCRPT_NM_EXEC_REQ.SCRPT_REQUEST_ID AS SCRPT_NM_EXEC_REQ, " +
//                    "SCRPT_EXEC_REQ.SCRPT_REQUEST_ID AS SCRPT_EXEC_REQ, " +
//                    "EXECUTION_REQUEST_LBL.REQUEST_ID AS EXECUTION_REQUEST_LBL, " +
//                    "COUNT(*) " +
//                    "FROM " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("ExecutionRequests").getName() +
//                    " AS EXECUTION_REQUEST " +
//
//                    " LEFT OUTER JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("AuthenticatedExecutionRequests").getName() + " AUTH_EXECUTION_REQUEST " +
//                    "ON EXECUTION_REQUEST.REQUEST_ID = AUTH_EXECUTION_REQUEST.REQUEST_ID " +
//
//                    "LEFT OUTER JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("NonAuthenticatedExecutionRequests").getName() + " NON_AUTH_EXECUTION_REQUEST " +
//                    "ON EXECUTION_REQUEST.REQUEST_ID = NON_AUTH_EXECUTION_REQUEST.REQUEST_ID " +
//
//                    "LEFT OUTER JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("ExecutionRequestLabels").getName() + " EXECUTION_REQUEST_LBL " +
//                    "ON EXECUTION_REQUEST.REQUEST_ID = EXECUTION_REQUEST_LBL.REQUEST_ID " +
//
//                    "LEFT OUTER  JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("ScriptExecutionRequests").getName() + " SCRPT_EXEC_REQ " +
//                    "ON EXECUTION_REQUEST.REQUEST_ID = SCRPT_EXEC_REQ.SCRPT_REQUEST_ID " +
//
//                    "LEFT OUTER  JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("ScriptNameExecutionRequests").getName() + " SCRPT_NM_EXEC_REQ " +
//                    "ON SCRPT_EXEC_REQ.SCRPT_REQUEST_ID = SCRPT_NM_EXEC_REQ.SCRPT_REQUEST_ID " +
//
//                    getFilterClause(filterColumn, searchParam, request_from, request_to) + " ;";
//
//            CachedRowSet cachedRowSet = metadataRepositoryConfiguration.getDesignMetadataRepository().executeQuery(query, "reader");
//            int result = 0;
//            while (cachedRowSet.next()) {
//                 result = cachedRowSet.getInt("COUNT(*)");
//            }
//            return (int) Math.ceil((double) result / limit);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String getOrderByStatement(List<String> columns, List<String> sorts) {
//        StringBuilder sqlQuery = new StringBuilder();
//        if (columns != null) {
//            if (columns.size() > 1) {
//                final String comma = ",";
//                for (int i = 0; i < columns.size(); i++) {
//                    String column = columns.get(i);
//                    String sort = sorts.get(i);
//                    sqlQuery.append(" " + orderByColumn(column) + " ").append(sort).append(comma);
//                }
//                if (!columns.isEmpty()) {
//                    sqlQuery.setLength(sqlQuery.length() - comma.length());
//                }
//            } else {
//                String column = columns.get(0);
//                sqlQuery.append(orderByColumn(column) + " ").append(sorts.get(0));
//            }
//            return sqlQuery.toString();
//        }
//        sqlQuery.append("EXECUTION_REQUEST.REQUEST_ID ").append(" ASC ");
//        return sqlQuery.toString();
//    }
//
//    public String orderByColumn(String column) {
//        switch (column) {
//            case "request_name":
//                return " EXECUTION_REQUEST.REQUEST_NM ";
//            case "request_timestamp":
//                return " EXECUTION_REQUEST.REQUEST_TMS ";
//            case "script_name":
//                return " SCRPT_NM_EXEC_REQ.SCRPT_NAME ";
//            case "script_version":
//                return " SCRPT_NM_EXEC_REQ.SCRPT_VRS ";
//            default:
//                throw new IllegalArgumentException("Wrong query on column");
//        }
//    }
//
//    public String getFilterClause(String filterColumn, String searchParam, String request_from, String request_to) {
//        StringBuilder sqlQuery = new StringBuilder();
//        if (filterColumn != null) {
//            if (filterColumn.equals("request_name")) {
//                sqlQuery.append("WHERE EXECUTION_REQUEST.REQUEST_NM LIKE " + SQLTools.GetStringForSQL(searchParam + "%"));
//            } else if (filterColumn.equals("script_name")) {
//                sqlQuery.append("WHERE SCRPT_NM_EXEC_REQ.SCRPT_NAME LIKE " + SQLTools.GetStringForSQL(searchParam + "%"));
//            } else if (filterColumn.equals("script_version")) {
//                sqlQuery.append("WHERE SCRPT_NM_EXEC_REQ.SCRPT_VRS LIKE " + SQLTools.GetStringForSQL(searchParam + "%"));
//            } else if (filterColumn.equals("script_environment")) {
//                sqlQuery.append("WHERE SCRPT_EXEC_REQ.ENVIRONMENT  LIKE " + SQLTools.GetStringForSQL(searchParam + "%"));
//            } else if (filterColumn.equals("execution_label")) {
//                List<String> searchParamSpit = Arrays.asList(searchParam.split(":"));
//                sqlQuery.append(" WHERE (EXECUTION_REQUEST_LBL.NAME LIKE " + SQLTools.GetStringForSQL(searchParamSpit.get(0) + "%") + ") AND ( EXECUTION_REQUEST_LBL.VALUE LIKE " + SQLTools.GetStringForSQL(searchParamSpit.get(1) + "%") + ")");
//            } else if (filterColumn.equals("request_timestamp") && request_to == null) {
//                sqlQuery.append(" WHERE  EXECUTION_REQUEST.REQUEST_TMS  LIKE " + SQLTools.GetStringForSQL(request_from + "%"));
//            } else if (filterColumn.equals("request_timestamp") && request_to != null) {
//                sqlQuery.append(" WHERE  EXECUTION_REQUEST.REQUEST_TMS  BETWEEN " + SQLTools.GetStringForSQL(request_from + "%") + " AND " + SQLTools.GetStringForSQL(request_to + "%"));
//            }
//        }
//        sqlQuery.append(" ");
//        return sqlQuery.toString();
//    }
//
//
//    public List<ExecutionRequest> getAll(int limit, int pageNumber, List<String> column, List<String> sort, String filterColumn, String searchParam, String request_from, String request_to) {
//        try {
//            List<ExecutionRequest> executionRequests = new ArrayList<>();
//
//            String query = "SELECT EXECUTION_REQUEST.REQUEST_ID, EXECUTION_REQUEST.REQUEST_TMS, EXECUTION_REQUEST.REQUEST_NM, " +
//                    "EXECUTION_REQUEST.REQUEST_DSC, EXECUTION_REQUEST.NOTIF_EMAIL, EXECUTION_REQUEST.SCOPE_NM, EXECUTION_REQUEST.CONTEXT_NM, EXECUTION_REQUEST.ST_NM, " +
//                    "AUTH_EXECUTION_REQUEST.SPACE_NM, AUTH_EXECUTION_REQUEST.USER_NM, AUTH_EXECUTION_REQUEST.USER_PASSWORD, " +
//                    "SCRPT_NM_EXEC_REQ.SCRPT_NAME, SCRPT_NM_EXEC_REQ.SCRPT_VRS, SCRPT_EXEC_REQ.ENVIRONMENT, " +
//                    "EXECUTION_REQUEST_LBL.NAME, EXECUTION_REQUEST_LBL.VALUE, " +
//
//                    "AUTH_EXECUTION_REQUEST.REQUEST_ID AS AUTH, " +
//                    "NON_AUTH_EXECUTION_REQUEST.REQUEST_ID AS NON_AUTH, " +
//                    "SCRPT_NM_EXEC_REQ.SCRPT_REQUEST_ID AS SCRPT_NM_EXEC_REQ, " +
//                    "SCRPT_EXEC_REQ.SCRPT_REQUEST_ID AS SCRPT_EXEC_REQ, " +
//                    "EXECUTION_REQUEST_LBL.REQUEST_ID AS EXECUTION_REQUEST_LBL " +
//
//                    "FROM (SELECT * FROM " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("ExecutionRequests").getName()
//                    + " LIMIT " + SQLTools.GetStringForSQL(limit) + " OFFSET (" + SQLTools.GetStringForSQL(pageNumber) + "-1 ) * "
//                    + SQLTools.GetStringForSQL(limit) + " ) " + " EXECUTION_REQUEST " +
//
//                    "LEFT OUTER JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("AuthenticatedExecutionRequests").getName() + " AUTH_EXECUTION_REQUEST " +
//                    "ON EXECUTION_REQUEST.REQUEST_ID = AUTH_EXECUTION_REQUEST.REQUEST_ID " +
//
//                    "LEFT OUTER JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("NonAuthenticatedExecutionRequests").getName() + " NON_AUTH_EXECUTION_REQUEST " +
//                    "ON EXECUTION_REQUEST.REQUEST_ID = NON_AUTH_EXECUTION_REQUEST.REQUEST_ID " +
//
//                    "LEFT OUTER JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("ExecutionRequestLabels").getName() + " EXECUTION_REQUEST_LBL " +
//                    "ON EXECUTION_REQUEST.REQUEST_ID = EXECUTION_REQUEST_LBL.REQUEST_ID " +
//
//                    "LEFT OUTER  JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("ScriptExecutionRequests").getName() + " SCRPT_EXEC_REQ " +
//                    "ON EXECUTION_REQUEST.REQUEST_ID = SCRPT_EXEC_REQ.SCRPT_REQUEST_ID " +
//
//                    "LEFT OUTER  JOIN " + MetadataTablesConfiguration.getInstance().getMetadataTableNameByLabel("ScriptNameExecutionRequests").getName() + " SCRPT_NM_EXEC_REQ " +
//                    "ON SCRPT_EXEC_REQ.SCRPT_REQUEST_ID = SCRPT_NM_EXEC_REQ.SCRPT_REQUEST_ID " +
//
//                    getFilterClause(filterColumn, searchParam, request_from, request_to)
//
//                    + " ORDER BY " + getOrderByStatement(column, sort) + ";";
//
//            CachedRowSet cachedRowSet = metadataRepositoryConfiguration.getDesignMetadataRepository().executeQuery(query, "reader");
//            while (cachedRowSet.next()) {
//
//                if (cachedRowSet.getString("AUTH") != null) {
//                    executionRequests.add(new AuthenticatedExecutionRequest(new ExecutionRequestKey(cachedRowSet.getString("REQUEST_ID")),
//                            SQLTools.getLocalDatetimeFromSql(cachedRowSet.getString("REQUEST_TMS")),
//                            cachedRowSet.getString("REQUEST_NM"),
//                            cachedRowSet.getString("REQUEST_DSC"),
//                            cachedRowSet.getString("NOTIF_EMAIL"),
//                            cachedRowSet.getString("SCOPE_NM"),
//                            cachedRowSet.getString("CONTEXT_NM"),
//                            ExecutionRequestStatus.valueOf(cachedRowSet.getString("ST_NM")),
//                            ScriptExecutionRequestConfiguration.getInstance().getByExecutionRequest(new ExecutionRequestKey(cachedRowSet.getString("REQUEST_ID"))),
//                            ExecutionRequestLabelConfiguration.getInstance().getByExecutionRequest(new ExecutionRequestKey(cachedRowSet.getString("REQUEST_ID"))),
//                            cachedRowSet.getString("SPACE_NM"),
//                            cachedRowSet.getString("USER_NM"), cachedRowSet.getString("USER_PASSWORD")));
//                } else if (cachedRowSet.getString("NON_AUTH") != null) {
//                    executionRequests.add(new NonAuthenticatedExecutionRequest(new ExecutionRequestKey(cachedRowSet.getString("REQUEST_ID")),
//                            SQLTools.getLocalDatetimeFromSql(cachedRowSet.getString("REQUEST_TMS")),
//                            cachedRowSet.getString("REQUEST_NM"),
//                            cachedRowSet.getString("REQUEST_DSC"),
//                            cachedRowSet.getString("NOTIF_EMAIL"),
//                            cachedRowSet.getString("SCOPE_NM"),
//                            cachedRowSet.getString("CONTEXT_NM"),
//                            ExecutionRequestStatus.valueOf(cachedRowSet.getString("ST_NM")),
//                            ScriptExecutionRequestConfiguration.getInstance().getByExecutionRequest(new ExecutionRequestKey(cachedRowSet.getString("REQUEST_ID"))),
//                            ExecutionRequestLabelConfiguration.getInstance().getByExecutionRequest(new ExecutionRequestKey(cachedRowSet.getString("REQUEST_ID")))));
//                } else {
//                    log.warn(MessageFormat.format("ExecutionRequest {0} does not have a certain class", cachedRowSet.getString("REQUEST_ID")));
//                }
//            }
//            return executionRequests;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}