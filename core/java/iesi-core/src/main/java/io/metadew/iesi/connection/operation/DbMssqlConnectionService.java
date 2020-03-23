package io.metadew.iesi.connection.operation;

import io.metadew.iesi.connection.database.MssqlDatabase;
import io.metadew.iesi.connection.database.connection.mssql.MssqlDatabaseConnection;
import io.metadew.iesi.framework.crypto.FrameworkCrypto;
import io.metadew.iesi.framework.execution.FrameworkControl;
import io.metadew.iesi.metadata.definition.connection.Connection;
import io.metadew.iesi.metadata.definition.connection.ConnectionType;
import lombok.extern.log4j.Log4j2;

import java.text.MessageFormat;
import java.util.List;

@Log4j2
public class DbMssqlConnectionService {

    private final static String hostKey = "host";
    private final static String portKey = "port";
    private final static String databaseKey = "database";
    private final static String userKey = "user";
    private final static String passwordKey = "password";

    private static DbMssqlConnectionService INSTANCE;

    public synchronized static DbMssqlConnectionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DbMssqlConnectionService();
        }
        return INSTANCE;
    }
    private DbMssqlConnectionService() {
    }

    public MssqlDatabase getDatabase(Connection connection) {

        String hostName = getMandatoryParameterWithKey(connection, hostKey);
        int port = Integer.parseInt(getMandatoryParameterWithKey(connection,portKey));
        String databaseName = getMandatoryParameterWithKey(connection, databaseKey);
        String userName = getMandatoryParameterWithKey(connection, userKey);
        String userPassword = getMandatoryParameterWithKey(connection, passwordKey);

        MssqlDatabaseConnection mssqlDatabaseConnection = new MssqlDatabaseConnection(hostName,
                port,
                databaseName,
                userName,
                userPassword);
        return new MssqlDatabase(mssqlDatabaseConnection, "");
    }

    private String getMandatoryParameterWithKey(Connection connection, String key) {
        return connection.getParameters().stream()
                .filter(connectionParameter -> connectionParameter.getName().equalsIgnoreCase(key))
                .findFirst()
                .map(connectionParameter -> FrameworkControl.getInstance().resolveConfiguration(connectionParameter.getValue()))
                .map(connectionParameterValue -> FrameworkCrypto.getInstance().decryptIfNeeded(connectionParameterValue))
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("Connection {0} does not contain mandatory parameter ''{1}''", connection, key)));

    }
}