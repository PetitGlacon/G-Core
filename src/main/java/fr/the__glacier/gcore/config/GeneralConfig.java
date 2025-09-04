package fr.the__glacier.gcore.config;

import fr.the__glacier.gcore.database.SQLType;

public class GeneralConfig {
    public SQL sqlInfos;
    public MessagesParts messagesParts;


    public GeneralConfig(){
        this.sqlInfos = new SQL();
        this.messagesParts = new MessagesParts();
    }
    public GeneralConfig(SQL sqlInfos, MessagesParts messagesParts){
        this.sqlInfos = sqlInfos;
        this.messagesParts = messagesParts;
    }

    public static class MessagesParts{
        public String day;
        public String days;
        public String hour;
        public String hours;
        public String minute;
        public String minutes;
        public String second;
        public String seconds;

        public MessagesParts(){
            this.day = " day ";
            this.days = " days ";
            this.hour = " hour ";
            this.hours = " hours ";
            this.minute = " minute ";
            this.minutes = " minutes ";
            this.second = " second ";
            this.seconds = " seconds ";
        }
    }

    public static class SQL {
        public SQLType SQLType;
        public String host;
        public int port;
        public String dataBase;
        public String userName;
        public String password;

        public SQL() {

            this.SQLType = fr.the__glacier.gcore.database.SQLType.SQLITE;
            this.host = "host";
            this.userName = "username";
            this.port = 3306;
            this.password = "password";
            this.dataBase = "database";
        }

        public SQL(SQLType sqLtype, String host, int port, String dataBase, String userName, String password) {
            this.SQLType = sqLtype;
            this.host = host;
            this.port = port;
            this.dataBase = dataBase;
            this.userName = userName;
            this.password = password;
        }
    }
}
