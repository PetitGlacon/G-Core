package fr.the__glacier.gcore.database;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.the__glacier.gcore.GCore;
import org.apache.logging.log4j.LogManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DatabasesManager {
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DatabasesManager.class);
    private HikariDataSource dataSource;
    private final Logger logger = GCore.getInstance().getLogger();
    public SQLType sqlType;

    public DatabasesManager(JavaPlugin plugin, SQLType type, String host, int port, String name, String username, String password){
        this.sqlType = type;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setLeakDetectionThreshold(3000);
        hikariConfig.setConnectionTimeout(5000);
        hikariConfig.setIdleTimeout(600000);

        switch (type) {
            case MYSQL -> {
                hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name);
                hikariConfig.setUsername(username);
                hikariConfig.setPassword(password);
                hikariConfig.setMaximumPoolSize(10);
                hikariConfig.setMinimumIdle(1);
            }
            case SQLITE -> {
                String path;
                if (host == null){
                    path = plugin.getDataFolder().getPath() + "/database/" + "/" + name + ".db";
                } else {
                    path = plugin.getDataFolder().getPath() + "/database/" + host + "/" + name + ".db";
                }
                File file = new File(path);
                file.getParentFile().mkdirs();

                String jdbcUrl = "jdbc:sqlite:" + path;
                hikariConfig.setJdbcUrl(jdbcUrl);
                hikariConfig.setMaximumPoolSize(1);
                hikariConfig.setMinimumIdle(1);
            }
            default -> throw new IllegalArgumentException("Type de données non pris en charge : " + type);
        }
        dataSource = new HikariDataSource(hikariConfig);
    }

    public void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()){
            dataSource.close();
        }
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                throw new IllegalStateException("Impossible de fermer la connection SQL.", e);
//            }
//        }
    }

    private Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

    public boolean tableExists(String tableName) {
        try (Connection connection = getConnection()){
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);
            return tables.next();
        } catch (SQLException e) {
            return false;
        }
    }

    private void executeUpdate(String request, String... parameters){
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            if (parameters != null){
                for (int i = 0; i < parameters.length; i++){
                    preparedStatement.setString(i+1, parameters[i]);
                }
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while executing an SQL request : " + request, e);
        }
    }
    private List<Map<String, Object>> executeQuery(String request, Object... parameters){
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(request);
            if (parameters != null){
                for (int i = 0; i < parameters.length; i++){
                    preparedStatement.setObject(i+1, parameters[i]);
                }
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                while (resultSet.next()){
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++){
                        map.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
                    }
                    result.add(map);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while executing an SQL request : " + request, e);
        }
        return result;
    }

    public void createTable(String name, String columnsType){
        String[] columns = columnsType.split("/");
        int nbColumns = columns.length;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
        for (int i = 0; i<nbColumns; i++){
            if (i != 0) queryBuilder.append(",");
            String[] columnWithTypes = columns[i].split(":");
            if (columnWithTypes.length != 2) {
                logger.severe("Spécification de type de colonne invalide pour la colonne " + i);
                return;
            }
            String columnName = columnWithTypes[0].toUpperCase();
            String columnType = columnWithTypes[1].toUpperCase();
            if (this.sqlType == SQLType.SQLITE){
                queryBuilder.append(columnName).append(" ").append(columnType.replace("INT AUTO_INCREMENT PRIMARY KEY", "INTEGER PRIMARY KEY AUTOINCREMENT"));
            } else {
                queryBuilder.append(columnName).append(" ").append(columnType);
            }
        }
        queryBuilder.append(")");
        String query = queryBuilder.toString();
        executeUpdate(query);
        logger.info("Table " + name + " créée avec succées dans la base de donnée.");
    }

    public List<Map<String, Object>> getAllData(String tablename){
        String request = "SELECT * FROM " + tablename;
        return executeQuery(request);
    }

    public List<Map<String, Object>> getDataListFromColumn(String tablename, String column){
        String request = "SELECT " + column + " FROM " + tablename;
        return executeQuery(request);
    }
    public List<Map<String, Object>> getDataListFromColumn(String tablename, String column, String conditions){
        String request = "SELECT " + column + " FROM " + tablename + " WHERE " + conditions;
        return executeQuery(request);
    }

    public String getDataString(String tablename, String column1, String column2, String valueColumn2){
        String request = "SELECT " + column1 + " FROM " + tablename + " WHERE " + column2 + " = ?";
        List<Map<String, Object>> list = executeQuery(request, valueColumn2);
        Object result = list.getFirst().get(column1);
        if (result != null && result instanceof String str){
            return str;
        } else {
            return null;
        }
    }


    public void addData(String tablename, ImmutableMap<BaseDataTable.ColumnIdentifier, String> values){
        StringBuilder columnsBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();

        for (Map.Entry<BaseDataTable.ColumnIdentifier, String> key : values.entrySet()) {
            columnsBuilder.append(key.getKey().getName()).append(", ");
            valuesBuilder.append("'").append(key.getValue().replaceAll("'", "`")).append("', ");
        }

        columnsBuilder.delete(columnsBuilder.length() - 2, columnsBuilder.length());
        valuesBuilder.delete(valuesBuilder.length() - 2, valuesBuilder.length());

        String request = "INSERT INTO " + tablename + " (" + columnsBuilder + ") VALUES (" + valuesBuilder + ")";

        executeUpdate(request);
    }

    public void updateData(String tablename, String column, String value, String comparator, String columnToChange, String valueToChange){
        String request = "UPDATE " + tablename + " SET " + columnToChange + " = '" + valueToChange + "' WHERE " + column + " " + comparator + " '" + value + "';";
        executeUpdate(request);
    }
    public void moveLinesFromDatabase(String from, String to, String condition){
        String request = "INSERT INTO " + to + " SELECT * FROM " + from + " WHERE " + condition + ";";
        executeUpdate(request);
    }
    public void removeData(String tablename, String condition){
        String request = "DELETE FROM " + tablename  + " WHERE " + condition + ";";
        executeUpdate(request);
    }
    public boolean dataFromDatabaseExist(String tablename, String column, String value){
        String request = "SELECT * FROM " + tablename + " WHERE " + column + " = ?";
        List<Map<String, Object>> list = executeQuery(request, value);
        return list.getFirst().get(column) != null;
    }

    public List<Map<String, Object>> getDataLine(String tablename, String column, String valueColumn){
        String request = "SELECT * FROM " + tablename + " WHERE " + column + " = ?";
        return executeQuery(request, valueColumn);
    }

    public List<Map<String, Object>> getTop(String tablename, String column, int nb){
        String request = "SELECT * FROM " + tablename + " ORDER BY " + column + " DESC LIMIT " + nb;
        return executeQuery(request);
    }
}
