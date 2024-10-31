package fr.the__glacier.gcore.database;

import com.google.common.collect.ImmutableMap;
import fr.the__glacier.gcore.GCore;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DatabasesManager {
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DatabasesManager.class);
    private Connection connection;
    private final Logger logger = GCore.getInstance().getLogger();
    public SQLType sqlType;

    public DatabasesManager(SQLType type, String host, int port, String name, String username, String password){
        switch (type) {
            case MYSQL -> createManager("jdbc:mysql://" + host + ":" + port + "/" + name, username, password);
            case SQLITE -> createManager(GCore.getInstance().getDataFolder().getPath() + "/database/" + host + "/", name + ".db");
            default -> throw new IllegalArgumentException("Type de données non pris en charge : " + type);
        }
    }
    private void createManager(String url, String username, String password){
        try {
            connection = DriverManager.getConnection(url, username, password);
            this.sqlType = SQLType.MYSQL;
            logger.info("Connection MySql confirmée");
        } catch (SQLException e) {
            throw new IllegalStateException("Impossible de se connecter à la base de données", e);
        }
    }
    private void createManager(String path, String fileName){
        try {
            File file = new File(path);
            boolean mkdirs = file.mkdirs();
            connection = DriverManager.getConnection("jdbc:sqlite:" + path + fileName);
            logger.info("Connection SqLite confirmée");
            this.sqlType = SQLType.SQLITE;
        } catch (SQLException e) {
            throw new IllegalStateException("Impossible de se connecter à la base de données", e);
        }
    }
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IllegalStateException("Impossible de fermer la connection SQL.", e);
            }
        }
    }

    public void createTable(String name, String columnsType){
        String[] columns = columnsType.split("/");
        int nbColumns = columns.length;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("REATE TABLE IF NOT EXISTS ").append(name).append(" (");
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

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
            logger.info("Table " + name + " créée avec succées dans la base de donnée.");
        } catch (SQLException e){
            logger.severe("Problème lors de la création de la table " + name + " dans la base de donnée.");
            logger.warning(query);
            log.log(Level.ERROR, e.getMessage(), e);
        }
    }
    public boolean tableExists(String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);
            return tables.next();
        } catch (SQLException e) {
            return false;
        }
    }
    public List<String> getDataListFromColumn(String tablename, String column){
        String request = "SELECT " + column + " FROM " + tablename;
        List<String> valeurs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultat = statement.executeQuery();

            while (resultat.next()) {
                String data = resultat.getString(column);
                valeurs.add(data);
            }
            resultat.close();
            return valeurs;
        } catch (SQLException e) {
            logger.severe("Impossible de récupérer les valeurs dans la colonne " + column + " de la table " + tablename + " dans la base de donnée.");
            logger.warning(request);
            log.log(Level.ERROR, e.getMessage(), e);
            return valeurs;
        }
    }
    public List<String> getDataListFromColumn(String tablename, String column, String conditions){
        String request = "SELECT " + column + " FROM " + tablename + " WHERE " + conditions;
        List<String> valeurs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultat = statement.executeQuery();

            while (resultat.next()) {
                String data = resultat.getString(column);
                valeurs.add(data);
            }
            resultat.close();
            return valeurs;
        } catch (SQLException e) {
            logger.severe("Impossible de récupérer les valeurs dans la colonne " + column + " de la table " + tablename + " dans la base de donnée.");
            logger.warning(request);
            log.log(Level.ERROR, e.getMessage(), e);
            return valeurs;
        }
    }

    public String getDataString(String tablename, String column1, String column2, String valueColumn2){
        String request = "SELECT " + column1 + " FROM " + tablename + " WHERE " + column2 + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, "'" +  valueColumn2 + "'");

            ResultSet resultat = statement.executeQuery();

            if (resultat.next()) {
                resultat.close();
                return resultat.getString(column1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.severe("Impossible de récupérer des valeurs dans la table " + tablename + " dans la base de donnée.");
            logger.warning(request);
            log.log(Level.ERROR, e.getMessage(), e);
            return null;
        }
    }


    public void addData(String tablename, ImmutableMap<BaseDataTable.ColumnIdentifier, String> values){
        StringBuilder columnsBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();

        for (Map.Entry<BaseDataTable.ColumnIdentifier, String> key : values.entrySet()) {
            columnsBuilder.append(key.getKey().getName()).append(", ");
            valuesBuilder.append("'").append(key.getValue().replace("'", "`")).append("', ");
        }

        columnsBuilder.delete(columnsBuilder.length() - 2, columnsBuilder.length());
        valuesBuilder.delete(valuesBuilder.length() - 2, valuesBuilder.length());

        String request = "INSERT INTO " + tablename + " (" + columnsBuilder + ") VALUES (" + valuesBuilder + ")";

        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.severe("Impossible d'ajouter des valeurs à " + tablename + " dans la base de donnée.");
            logger.warning(request);
            log.log(Level.ERROR, e.getMessage(), e);
        }
    }

    public void updateData(String tablename, String column, String value, String comparator, String columnToChange, String valueToChange){
        String request = "UPDATE " + tablename + " SET " + columnToChange + " = '" + valueToChange + "' WHERE " + column + " " + comparator + " '" + value + "';";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.severe("Impossible de changer des valeurs à " + tablename + " dans la base de donnée.");
            logger.warning(request);
            log.log(Level.ERROR, e.getMessage(), e);
        }
    }
    public void moveLinesFromDatabase(String from, String to, String condition){
        String request = "INSERT INTO " + to + " SELECT * FROM " + from + " WHERE " + condition + ";";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.severe("Impossible de changer des valeurs de " + from + " à " + to  + " dans la base de donnée.");
            logger.warning(request);
            log.log(Level.ERROR, e.getMessage(), e);
        }
    }
    public void removeData(String tablename, String condition){
        String request = "DELETE FROM " + tablename  + " WHERE " + condition + ";";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.severe("Impossible de supprimer des valeurs de " + tablename + " dans la base de donnée.");
            logger.warning(request);
            log.log(Level.ERROR, e.getMessage(), e);
        }
    }
    public boolean dataFromDatabaseExist(String tablename, String column, String value){
        String request = "SELECT * FROM " + tablename + " WHERE " + column + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, value);
            ResultSet resultat = statement.executeQuery();
            if (resultat.next()){
                String str = resultat.getString(1);
                resultat.close();
                return str != null;
            } else return false;
        } catch (SQLException e){
            logger.severe("Impossible de récupérer des valeurs dans la table " + tablename + " dans la base de donnée.");
            logger.warning(request + " / replace ? with " + value);
            log.log(Level.ERROR, e.getMessage(), e);
            return false;
        }
    }

    public List<String> getDataLine(String tablename, String column, String valueColumn, int nbColonnes){
        String request = "SELECT * FROM " + tablename + " WHERE " + column + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, valueColumn);

            try {
                ResultSet resultat = statement.executeQuery();
                List<String> data = new ArrayList<>();
                if (resultat.next()) {
                    for (int i = 1; i <= nbColonnes; i++) {
                        data.add(resultat.getString(i).replace("`", "'"));
                    }
                }
                resultat.close();
                return data;
            } catch (SQLException e){
                logger.severe("Impossible de récupérer des valeurs dans la table " + tablename + " dans la base de donnée.");
                logger.warning(request + " / replace ? with " + valueColumn);
                logger.warning(statement.toString());
                log.log(Level.ERROR, e.getMessage(), e);
                return null;
            }
        } catch (SQLException e) {
            logger.severe("Impossible de récupérer des valeurs dans la table " + tablename + " dans la base de donnée.");
            logger.warning(request + " / replace ? with " + valueColumn);
            log.log(Level.ERROR, e.getMessage(), e);
            return null;
        }
    }
}
