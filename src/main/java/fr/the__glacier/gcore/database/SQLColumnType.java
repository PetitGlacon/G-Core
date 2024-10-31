package fr.the__glacier.gcore.database;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SQLColumnType {
    // Getter for the type name
    private final String typeName;
    // Getter and Setter for length
    @Setter
    private Integer length;

    // Constructor for types without length
    public SQLColumnType(String typeName) {
        this.typeName = typeName;
    }

    // Constructor for types with length
    public SQLColumnType(String typeName, int length) {
        this.typeName = typeName;
        this.length = length;
    }

    // Method to represent the type with its parameter if applicable
    @Override
    public String toString() {
        if (length != null) {
            return typeName + "(" + length + ")";
        }
        return typeName;
    }

    // Static factory methods for each type

    // Numeric Types
    public static SQLColumnType TINYINT() {
        return new SQLColumnType("TINYINT");
    }

    public static SQLColumnType SMALLINT() {
        return new SQLColumnType("SMALLINT");
    }

    public static SQLColumnType MEDIUMINT() {
        return new SQLColumnType("MEDIUMINT");
    }

    public static SQLColumnType INT() {
        return new SQLColumnType("INT");
    }
    public static SQLColumnType INT_auto_increment(){
        return new SQLColumnType("INT AUTO_INCREMENT PRIMARY KEY");
    }

    public static SQLColumnType BIGINT() {
        return new SQLColumnType("BIGINT");
    }

    public static SQLColumnType FLOAT() {
        return new SQLColumnType("FLOAT");
    }

    public static SQLColumnType DOUBLE() {
        return new SQLColumnType("DOUBLE");
    }

    public static SQLColumnType DECIMAL() {
        return new SQLColumnType("DECIMAL");
    }

    public static SQLColumnType NUMERIC() {
        return new SQLColumnType("NUMERIC");
    }

    // Date and Time Types
    public static SQLColumnType DATE() {
        return new SQLColumnType("DATE");
    }

    public static SQLColumnType TIME() {
        return new SQLColumnType("TIME");
    }

    public static SQLColumnType DATETIME() {
        return new SQLColumnType("DATETIME");
    }

    public static SQLColumnType TIMESTAMP() {
        return new SQLColumnType("TIMESTAMP");
    }

    public static SQLColumnType YEAR() {
        return new SQLColumnType("YEAR");
    }

    // String Types
    public static SQLColumnType CHAR(int length) {
        if (length >= 255) return new SQLColumnType("CHAR", 255);
        else return new SQLColumnType("CHAR", length);
    }

    public static SQLColumnType VARCHAR(int length) {
        return new SQLColumnType("VARCHAR", length);
    }

    public static SQLColumnType BINARY(int length) {
        return new SQLColumnType("BINARY", length);
    }

    public static SQLColumnType VARBINARY(int length) {
        return new SQLColumnType("VARBINARY", length);
    }

    public static SQLColumnType TINYTEXT() {
        return new SQLColumnType("TINYTEXT");
    }

    public static SQLColumnType TEXT() {
        return new SQLColumnType("TEXT");
    }

    public static SQLColumnType MEDIUMTEXT() {
        return new SQLColumnType("MEDIUMTEXT");
    }

    public static SQLColumnType LONGTEXT() {
        return new SQLColumnType("LONGTEXT");
    }

    public static SQLColumnType TINYBLOB() {
        return new SQLColumnType("TINYBLOB");
    }

    public static SQLColumnType BLOB() {
        return new SQLColumnType("BLOB");
    }

    public static SQLColumnType MEDIUMBLOB() {
        return new SQLColumnType("MEDIUMBLOB");
    }

    public static SQLColumnType LONGBLOB() {
        return new SQLColumnType("LONGBLOB");
    }

    // JSON and Others
    public static SQLColumnType JSON() {
        return new SQLColumnType("JSON");
    }

    public static SQLColumnType ENUM() {
        return new SQLColumnType("ENUM");
    }

    public static SQLColumnType SET() {
        return new SQLColumnType("SET");
    }

    // Add more static factory methods for other types as needed
}

