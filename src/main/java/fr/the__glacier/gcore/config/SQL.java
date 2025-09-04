package fr.the__glacier.gcore.config;

import fr.the__glacier.gcore.database.SQLType;

public class SQL {
    public SQLType SQLType;
    public String host;
    public int port;
    public String dataBase;
    public String userName;
    public String password;
    public SQL(){
        this.SQLType = SQLType.SQLITE;
        this.host = "host";
        this.userName = "username";
        this.port = 3306;
        this.password = "password";
        this.dataBase = "database";
    }
    public SQL(SQLType sqLtype, String host, int port, String dataBase, String userName, String password){
        this.SQLType = sqLtype;
        this.host = host;
        this.port = port;
        this.dataBase = dataBase;
        this.userName = userName;
        this.password = password;
    }

}
