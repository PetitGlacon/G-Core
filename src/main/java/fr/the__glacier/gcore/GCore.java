package fr.the__glacier.gcore;

import fr.the__glacier.gcore.commands.GCoreUtils;
import fr.the__glacier.gcore.commands.utils.BrigadierCommands;
import fr.the__glacier.gcore.commands.utils.SubCommandsManager;
import fr.the__glacier.gcore.commands.gcore.GCoreCommand;
import fr.the__glacier.gcore.config.Commands;
import fr.the__glacier.gcore.config.SQL;
import fr.the__glacier.gcore.config.configObjects.CommandConfig;
import fr.the__glacier.gcore.database.DatabasesManager;
import fr.the__glacier.gcore.database.UserTable;
import fr.the__glacier.gcore.listener.CommandCompletionListener;
import fr.the__glacier.gcore.listener.PlayerJoinListener;
import fr.the__glacier.gcore.listener.PlayerLeaveListener;
import fr.the__glacier.gcore.test.ConfigTest;
import fr.the__glacier.gcore.test.Listeners;
import de.tr7zw.changeme.nbtapi.NBT;
import fr.the__glacier.gcore.util.PagedMessageManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class GCore extends JavaPlugin {
    @Getter
    private static GCore instance;

    private ConfigurationManager configurationManager;
    private SQL sql;
    private Commands commands;

    private DatabasesManager databasesManager;

    public UserTable userTable;


    public int VERSION;
    public static boolean isFolia = folia();

    public final PagedMessageManager pagedMessagesManager = new PagedMessageManager();

    private ConfigTest configTest;
    private ConfigTest configTest2;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().severe("true");
        VERSION = getVersion();
        if (!NBT.preloadApi()){
            getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        configurationManager = new ConfigurationManager(ConfigurationManager.PersistType.YAML, this);
        loadConfig();
        saveConfig();
        configTest.load();
        this.databasesManager = new DatabasesManager(sql.SQLType, sql.host, sql.port, sql.dataBase, sql.userName, sql.password);
        loadTables();
        registerListeners();
        //Test();
        registerCommands();

    }

    @Override
    public void onDisable() {
        userTable.databasesManager.closeConnection();
    }

    public void Test(){
        getServer().getPluginCommand("gcore");
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
    }

    public void registerCommands(){
        registerCommand(new GCoreCommand(this, new SubCommandsManager(), commands.GCoreCMD));
        registerCommand(new GCoreUtils(this, new SubCommandsManager(), new CommandConfig("gcoreutils")));
    }

    public void registerCommand(BrigadierCommands command){
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(command.getCommand().build());
        });
    }

    public void loadConfig(){
        configTest = configurationManager.load(ConfigTest.class);
        configTest2 = configurationManager.load(ConfigTest.class);
        sql = configurationManager.load(SQL.class);
        commands = configurationManager.load(Commands.class);
    }

    public void saveConfig(){
        configurationManager.save(configTest);
        configurationManager.saveWithFolders(configTest2, "fichier", "bonjour", "test", "test 2");
        configurationManager.save(sql);
        configurationManager.save(commands);
    }

    public void loadTables(){
        userTable = new UserTable(databasesManager, "Users");
    }

    public void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandCompletionListener(), this);
    }

    private static int getVersion() {
        String version = Bukkit.getVersion();
        int index = version.lastIndexOf("MC:");
        if (index != -1) {
            version = version.substring(index + 4, version.length() - 1);
        } else if (version.endsWith("SNAPSHOT")) {
            index = version.indexOf(45);
            version = version.substring(0, index);
        }
        int lastDot = version.lastIndexOf(46);
        if (version.indexOf(46) != lastDot) {
            version = version.substring(0, lastDot);
        }
        return Integer.parseInt(version.substring(2));
    }
    public static boolean folia(){
        return classExist("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
    }
    public static boolean classExist(String string){
        try {
            Class.forName(string);
            return true;
        } catch (ClassNotFoundException e){
            return false;
        }
    }
}
