package io.metadew.iesi.connection.service;

import io.metadew.iesi.connection.database.OracleDatabase;
import io.metadew.iesi.connection.database.connection.DatabaseConnectionHandlerImpl;
import io.metadew.iesi.connection.database.connection.oracle.OracleDatabaseConnection;
import io.metadew.iesi.connection.operation.DbOracleConnectionService;
import io.metadew.iesi.connection.operation.DbTeradataConnectionService;
import io.metadew.iesi.framework.crypto.FrameworkCrypto;
import io.metadew.iesi.metadata.definition.connection.Connection;
import io.metadew.iesi.metadata.definition.connection.ConnectionParameter;
import io.metadew.iesi.metadata.definition.connection.key.ConnectionKey;
import io.metadew.iesi.metadata.definition.connection.key.ConnectionParameterKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.text.MessageFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class DbOracleConnectionServiceTest {
    @BeforeAll
    static void setup()  {
        DatabaseConnectionHandlerImpl databaseConnectionHandler = PowerMockito.mock(DatabaseConnectionHandlerImpl.class);
        Whitebox.setInternalState(DatabaseConnectionHandlerImpl.class, "INSTANCE", databaseConnectionHandler);
        Mockito.doReturn(null).when(databaseConnectionHandler).getConnection(any());
    }
    @Test
    void getDatabaseTest() {
        Connection connection = new Connection(new ConnectionKey("test", "tst"),
                "db.oracle",
                "description",
                Stream.of(
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "host"), "host"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "port"), "1"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "database"), "database"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "connectionUrl"), "connectionUrl"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "user"), "user"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "password"), "password"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "schema"), "schema"))
                        .collect(Collectors.toList()));
        OracleDatabase oracleDatabaseExpected = new OracleDatabase(new OracleDatabaseConnection("connectionUrl", "user",  "password"),4,8,"");
        assertEquals(oracleDatabaseExpected, DbOracleConnectionService.getInstance().getDatabase(connection));
    }
    @Test
    void getDatabaseWithEncryptedPasswordTest() {
        Connection connection = new Connection(new ConnectionKey("test", "tst"),
                "db.oracle",
                "description",
                Stream.of(
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "host"), "host"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "port"), "1"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "database"), "database"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "connectionUrl"), "connectionUrl"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "user"), "user"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "password"),  FrameworkCrypto.getInstance().encrypt("encrypted_password")),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "schema"), "schema"))
                        .collect(Collectors.toList()));
        OracleDatabase oracleDatabaseExpected = new OracleDatabase(new OracleDatabaseConnection("connectionUrl", "user",  "encrypted_password"),4,8,"");
        assertEquals(oracleDatabaseExpected, DbOracleConnectionService.getInstance().getDatabase(connection));
    }

    @Test
    void getDatabaseMissingHost() {
        Connection connection = new Connection(new ConnectionKey("test", "tst"),
                "db.oracle",
                "description",
                Stream.of(
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "port"), "1"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "database"), "database"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "connectionUrl"), "connectionUrl"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "user"), "user"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "password"), "password"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "schema"), "schema"))
                        .collect(Collectors.toList()));
        assertThrows(RuntimeException.class, () -> DbTeradataConnectionService.getInstance().getDatabase(connection),
                MessageFormat.format("Connection {0} does not contain mandatory parameter 'host'", connection));
    }

    @Test
    void getDatabaseMissingPort() {
        Connection connection = new Connection(new ConnectionKey("test", "tst"),
                "db.oracle",
                "description",
                Stream.of(
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "host"), "host"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "database"), "database"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "connectionUrl"), "connectionUrl"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "user"), "user"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "password"), "password"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "schema"), "schema"))
                        .collect(Collectors.toList()));
        assertThrows(RuntimeException.class, () -> DbTeradataConnectionService.getInstance().getDatabase(connection),
                MessageFormat.format("Connection {0} does not contain mandatory parameter 'port'", connection));
    }

    @Test
    void getDatabaseMissingDatabase() {
        Connection connection = new Connection(new ConnectionKey("test", "tst"),
                "db.oracle",
                "description",
                Stream.of(
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "host"), "host"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "port"), "1"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "connectionUrl"), "connectionUrl"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "user"), "user"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "password"), "password"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "schema"), "schema"))
                        .collect(Collectors.toList()));
        assertThrows(RuntimeException.class, () -> DbTeradataConnectionService.getInstance().getDatabase(connection),
                MessageFormat.format("Connection {0} does not contain mandatory parameter 'database'", connection));
    }

    @Test
    void getDatabaseMissingUser() {
        Connection connection = new Connection(new ConnectionKey("test", "tst"),
                "db.oracle",
                "description",
                Stream.of(
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "host"), "host"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "port"), "1"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "database"), "database"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "connectionUrl"), "connectionUrl"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "password"), "password"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "schema"), "schema"))
                        .collect(Collectors.toList()));
        assertThrows(RuntimeException.class, () -> DbTeradataConnectionService.getInstance().getDatabase(connection),
                MessageFormat.format("Connection {0} does not contain mandatory parameter 'user'", connection));
    }

    @Test
    void getDatabaseMissingPassword() {
        Connection connection = new Connection(new ConnectionKey("test", "tst"),
                "db.oracle",
                "description",
                Stream.of(
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "host"), "host"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "port"), "1"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "database"), "database"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "connectionUrl"), "connectionUrl"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "user"), "user"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "schema"), "schema"))
                        .collect(Collectors.toList()));
        assertThrows(RuntimeException.class, () -> DbTeradataConnectionService.getInstance().getDatabase(connection),
                MessageFormat.format("Connection {0} does not contain mandatory parameter 'password'", connection));
    }
    @Test
    void getDatabaseMissingConnectonUrl() {
        Connection connection = new Connection(new ConnectionKey("test", "tst"),
                "db.oracle",
                "description",
                Stream.of(
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "host"), "host"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "port"), "1"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "database"), "database"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "user"), "user"),
                        new ConnectionParameter(new ConnectionParameterKey("test", "tst", "schema"), "schema"))
                        .collect(Collectors.toList()));
        assertThrows(RuntimeException.class, () -> DbTeradataConnectionService.getInstance().getDatabase(connection),
                MessageFormat.format("Connection {0} does not contain mandatory parameter 'connectionUrl'", connection));
    }
}