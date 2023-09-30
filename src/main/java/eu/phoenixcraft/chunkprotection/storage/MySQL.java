package eu.phoenixcraft.chunkprotection.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQL {

    private final String jdbcUrl;
    private final String username;
    private final String password;
    public static Connection connection;

    public MySQL(String host, int port, String database, String username, String password) {
        this.jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;
    }

    public boolean connect() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            createClaimedChunksTable();
            createChunkSettingsTable();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void executeStatement(String sql) {
        if (connection == null) {
            throw new IllegalStateException("Die Verbindung zur Datenbank wurde nicht hergestellt.");
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createClaimedChunksTable() {
        String sql = "CREATE TABLE IF NOT EXISTS claimed_chunks ( " +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "player_uuid VARCHAR(36), " +
                "chunk_id BIGINT, " +
                "chunk_x INT, " +
                "chunk_z INT, " +
                "price BIGINT DEFAULT 0.0, " +
                "resell BOOLEAN DEFAULT FALSE, " +
                "world_name VARCHAR(255))"; // Füge die Spalte für den Weltnamen hinzu
        executeStatement(sql);
    }

    public void createChunkPermssionTable(){
        String sql = "CREATE TABLE IF NOT EXISTS permssion ( " +
                "player_uuid VARCHAR(36)," +
                "wolrd_name VARCHAR(50)," +
                "chunk_id BIGINT, " +
                "permssion VARCHAR(100)";
        executeStatement(sql);
    }


    public void createChunkSettingsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS chunk_settings ( " +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "chunk_id INT, " +
                "setting_name VARCHAR(50), " +
                "setting_value VARCHAR(255))";
        executeStatement(sql);
    }

    // Weitere Methoden zum Ausführen von Abfragen und Aktualisieren der Datenbank können hier hinzugefügt werden.
}