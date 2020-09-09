package io.metadew.iesi.connection.database.bigquery;

import io.metadew.iesi.connection.database.bigquery.BigqueryDatabaseConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ServiceBigqueryDatabaseConnection extends BigqueryDatabaseConnection {

    public ServiceBigqueryDatabaseConnection(String hostName, int portNumber, String project, String serviceAccount, String keyPath) {
        super(getConnectionUrl(hostName, portNumber, project, serviceAccount, keyPath));
    }

    private static String getConnectionUrl(String hostName, int portNumber, String project, String serviceAccount, String keyPath) {
        StringBuilder connectionUrl = new StringBuilder();
        connectionUrl.append("jdbc:bigquery://");
        connectionUrl.append(hostName);
        if (portNumber > 0) {
            connectionUrl.append(":");
            connectionUrl.append(portNumber);
        }
        connectionUrl.append(";ProjectId=");
        connectionUrl.append(project);
        connectionUrl.append(";OAuthType=");
        connectionUrl.append("0");
        connectionUrl.append(";OAuthServiceAcctEmail=");
        connectionUrl.append(serviceAccount);
        connectionUrl.append(";OAuthPvtKeyPath=");
        connectionUrl.append(keyPath);
        connectionUrl.append(";");
        return connectionUrl.toString();
    }

}
