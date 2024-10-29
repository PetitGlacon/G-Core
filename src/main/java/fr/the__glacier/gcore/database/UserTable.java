package fr.the__glacier.gcore.database;

import com.google.common.collect.ImmutableMap;
import fr.the__glacier.gcore.GCore;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UserTable extends BaseDataTable {

    public HashMap<String, User> userByName = new HashMap<>();
    public HashMap<UUID, User> userByUUID = new HashMap<>();
    public UserTable(DatabasesManager databasesManager, String tableName) {
        super(databasesManager, tableName);
        columnsNames.put(ColumnsNames.ID, SQLColumnType.INT_auto_increment());
        columnsNames.put(ColumnsNames.UUID, SQLColumnType.VARCHAR(36));
        columnsNames.put(ColumnsNames.PSEUDO, SQLColumnType.CHAR(255));
        columnsNames.put(ColumnsNames.FIRSTJOINTIME, SQLColumnType.BIGINT());
        columnsNames.put(ColumnsNames.LASTJOINTIME, SQLColumnType.BIGINT());
        columnsNames.put(ColumnsNames.LASTLEAVETIME, SQLColumnType.BIGINT());
        createTable();
        CompletableFuture.runAsync(this::loadDataFromDatabase);
    }

    @Override
    public void loadDataFromDatabase() {
        List<String> var1 = databasesManager.getDataListFromColumn(tableName, ColumnsNames.ID.getName());
        for (String str : var1){
            List<String> var2 = databasesManager.getDataLine(tableName, ColumnsNames.ID.getName(), str, ColumnsNames.values().length);
            try {
                int id = Integer.parseInt(var2.get(0));
                UUID uuid = UUID.fromString(var2.get(1));
                String name = var2.get(2);
                long firstJoinTime = Long.parseLong(var2.get(3));
                long lastJoinTime = Long.parseLong(var2.get(4));
                long lastLeaveTime = Long.parseLong(var2.get(5));

                addUserFromDB(new User(id, uuid, name, firstJoinTime, lastJoinTime, lastLeaveTime));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        GCore.getInstance().getLogger().warning("Data loaded from " + tableName);
    }
    public User getUser(String pseudo){
        return userByName.get(pseudo);
    }
    public User getUser(Player p){
        return getUser(p.getUniqueId());
    }
    public User getUser(UUID uuid){
        return userByUUID.get(uuid);
    }

    public void UserJoin(Player p){
        if (!isPlayerInDb(p)){
            User u = new User(p.getUniqueId(), p.getName(), p.getFirstPlayed(), p.getLastPlayed());
            CompletableFuture.runAsync(() -> {
                addUserToDB(u);
                if (isPlayerInDb(p)){
                    u.setID(getId(p));
                } else {
                    Bukkit.getLogger().severe("Attention, mauvaise récupération de l'ID dans la base de donnée " + tableName);
                }
            });
        } else {
            User u = userByUUID.get(p.getUniqueId());
            long lastTimeJoin = (new Date()).getTime();
            updateUser(u, ColumnsNames.LASTJOINTIME, String.valueOf(lastTimeJoin));
            if (!Objects.equals(u.getName(), p.getName())){
                updateUser(u, ColumnsNames.PSEUDO, p.getName());
            }
        }
    }
    public void UserLeave(Player p){
        if (!isPlayerInDb(p)){
            Bukkit.getLogger().severe("Impossible de récupérer le joueur " + p.getName() + " dans la base de donnée !");
        } else {
            User u = userByUUID.get(p.getUniqueId());
            long lastTimeLeave = (new Date()).getTime();
            updateUser(u, ColumnsNames.LASTLEAVETIME, String.valueOf(lastTimeLeave));
        }
    }
    public void updateUser(User u, ColumnsNames column, String value){
        int id = u.getID();
        CompletableFuture.runAsync(()-> {
            databasesManager.updateData(tableName, ColumnsNames.ID.getName(), String.valueOf(id), "=", column.getName(), value);
            switch (column) {
                case PSEUDO -> {
                    String name = u.getName();
                    u.setName(value);
                    userByUUID.put(u.getUuid(), u);
                    userByName.put(value, u);
                    userByName.remove(name);
                }
                case LASTJOINTIME -> {
                    u.setLastJoinTime(Long.parseLong(value));
                    userByUUID.put(u.getUuid(), u);
                    userByName.put(u.getName(), u);
                }
                case LASTLEAVETIME -> {
                    u.setLastLeaveTime(Long.parseLong(value));
                    userByUUID.put(u.getUuid(), u);
                    userByName.put(u.getName(), u);
                }
                default -> Bukkit.getLogger().severe("On essaye de changer une donnée inchangeable !");
            }
        });


    }
    public int getId(Player p){
        return Integer.parseInt(databasesManager.getDataString(tableName, ColumnsNames.ID.getName(), ColumnsNames.UUID.getName(), p.getUniqueId().toString()));
    }
    public boolean isPlayerInDb(Player p){
        return userByUUID.containsKey(p.getUniqueId());
    }
    public void addUserFromDB(User u){
        userByName.put(u.name, u);
        userByUUID.put(u.uuid, u);
    }

    public void addUserToDB(User u){
        userByName.put(u.name, u);
        userByUUID.put(u.uuid, u);
        CompletableFuture.runAsync(()->{
            databasesManager.addData(tableName, ImmutableMap.<ColumnIdentifier, String>builder()
                    .put(ColumnsNames.UUID, u.getUuid().toString())
                    .put(ColumnsNames.PSEUDO, u.getName())
                    .put(ColumnsNames.FIRSTJOINTIME, String.valueOf(u.getFirstJoinTime()))
                    .put(ColumnsNames.LASTJOINTIME, String.valueOf(u.getLastJoinTime()))
                    .put(ColumnsNames.LASTLEAVETIME, String.valueOf(u.getLastLeaveTime()))
                    .build());
        });
    }

    public enum ColumnsNames implements ColumnIdentifier {
        ID,
        UUID,
        PSEUDO,
        FIRSTJOINTIME,
        LASTJOINTIME,
        LASTLEAVETIME;

        @Override
        public String getName() {
            return name();
        }
    }

    @Getter
    public class User {
        public User(){}
        public User(UUID uuid, String name, long firstJoinTime, long lastJoinTime){
            this.uuid = uuid;
            this.name = name;
            this.firstJoinTime = firstJoinTime;
            this.lastJoinTime = lastJoinTime;
        }
        public User(int id, UUID uuid, String name, long firstJoinTime, long lastJoinTime, long lastLeaveTime){
            this.ID = id;
            this.uuid = uuid;
            this.name = name;
            this.firstJoinTime = firstJoinTime;
            this.lastJoinTime = lastJoinTime;
            this.lastLeaveTime = lastLeaveTime;
        }
        @Setter
        private int ID;
        private UUID uuid;

        @Setter
        private String name;

        private long firstJoinTime;

        @Setter
        private long lastJoinTime;

        @Setter
        private long lastLeaveTime;
        public boolean isOnline(){
            return lastJoinTime > lastLeaveTime;
        }

    }
}
