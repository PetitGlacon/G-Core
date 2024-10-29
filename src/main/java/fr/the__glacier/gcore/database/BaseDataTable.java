package fr.the__glacier.gcore.database;


import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseDataTable {
    public final DatabasesManager databasesManager;
    public final String tableName;
    public LinkedHashMap<ColumnIdentifier, SQLColumnType> columnsNames = new LinkedHashMap<>();

    public BaseDataTable(DatabasesManager databasesManager, String tableName){
        this.databasesManager = databasesManager;
        this.tableName = tableName;
    }
    public boolean tableExist(){
        return databasesManager.tableExists(tableName);
    }

    public void createTable() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<ColumnIdentifier, SQLColumnType> entry : columnsNames.entrySet()){
            stringBuilder.append(entry.getKey().getName()).append(":").append(entry.getValue().toString()).append("/");
        }
        String str = stringBuilder.substring(0, stringBuilder.length() - 1);
        databasesManager.createTable(tableName, str);
    }
    public abstract void loadDataFromDatabase();
    public interface ColumnIdentifier {
        String getName();
    }
}
