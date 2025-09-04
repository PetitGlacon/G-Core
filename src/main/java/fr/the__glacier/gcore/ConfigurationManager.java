package fr.the__glacier.gcore;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Class which persists configuration files.
 */


public class ConfigurationManager {

    private final ObjectMapper objectMapper;
    private final PersistType persistType;
    private final JavaPlugin javaPlugin;

    /**
     * The default constructor.
     *
     * @param persistType The format which should be used for persisting
     * @param javaPlugin  The plugin which persists. Parameter for dependency injection
     */
    public ConfigurationManager(PersistType persistType, JavaPlugin javaPlugin) {

        this.persistType = persistType;
        this.javaPlugin = javaPlugin;
        javaPlugin.getDataFolder().mkdir();

        objectMapper = new ObjectMapper(persistType.getFactory()).configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    /**
     * The name of a provided class.
     * Used as the file name for persisting.
     *
     * @param clazz The class whose name is to be returned
     * @return The name of the configuration file
     */
    private static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
    }

    // ------------------------------------------------------------ //
    // GET NAME - What should we call this type of object?
    // ------------------------------------------------------------ //

    /**
     * The name of a provided object.
     * Used as the file name for persisting.     *
     *
     * @param instance The object whose name is to be returned
     * @return The name of the configuration file
     */
    public static String getName(Object instance) {
        return getName(instance.getClass());
    }

    /**
     * The name of a provided type.
     * Used as the file name for persisting.
     *
     * @param type The type whose name is to be returned
     * @return The name of the configuration file
     * @see Type
     */
    public static String getName(Type type) {
        return getName(type.getClass());
    }

    // ------------------------------------------------------------ //
    // GET FILE - In which file would we like to store this object?
    // ------------------------------------------------------------ //

    /**
     * The file corresponding to the name.
     * Used for serialization.
     *
     * @param name The name of the configuration without file extension
     * @return The file where the config with the name should be stored
     */
    public File getFile(String name) {
        return new File(javaPlugin.getDataFolder(), name + persistType.getExtension());
    }

    /**
     * The file corresponding to the class.
     * Used for serialization.
     *
     * @param clazz The class which should be stored
     * @return The file where the config with the name should be stored
     */
    public File getFile(Class<?> clazz) {
        return getFile(getName(clazz));
    }

    /**
     * The file corresponding to the object.
     * Used for serialization.
     *
     * @param instance The object which should be stored
     * @return The file where the config with the name should be stored
     */
    public File getFile(Object instance) {
        return getFile(getName(instance));
    }

    // SAVE

    /**
     * Saves an object to a file.
     *
     * @param instance The instance of the object which should be saved
     */
    public void save(Object instance) {
        saveFile(instance, getFile(instance));
    }

    public void save(Object instance, String path){

        saveFile(instance, getFile(path + getName(instance)));
    }
    public void saveWithFolders(Object instance,String name, String... folders){
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : folders){
            stringBuilder.append(str);
            stringBuilder.append(File.separator);
            File file = new File(javaPlugin.getDataFolder().getPath() + File.separator + stringBuilder.substring(0, stringBuilder.length()));
            if (!file.exists()){
                javaPlugin.getLogger().info(file.getPath());
                file.mkdir();
            }
        }
        javaPlugin.getLogger().info(stringBuilder.substring(0, stringBuilder.length()));
        save(instance, stringBuilder.substring(0, stringBuilder.length()), name);
    }
    public void save(Object instance, String path, String name){

        saveFile(instance, getFile(path + name));
    }
    public void saveWithFolder(String folderName, Object instance, String... folders){
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : folders){
            stringBuilder.append(str);
            stringBuilder.append(File.separator);
            File file = new File(javaPlugin.getDataFolder().getPath() + File.separator + stringBuilder.substring(0, stringBuilder.length()));
            if (!file.exists()){
                javaPlugin.getLogger().info(file.getPath());
                file.mkdir();
            }
        }
        javaPlugin.getLogger().info(stringBuilder.substring(0, stringBuilder.length()));
        save(instance, stringBuilder.substring(0, stringBuilder.length()), folderName);
    }

    /**
     * Saves an object to a file.
     *
     * @param instance The instance of the object which should be saved
     * @param file     The file where the object should be persisted to
     */
    public void saveFile(Object instance, File file) {
        try {
            objectMapper.writeValue(file, instance);
        } catch (IOException e) {
            javaPlugin.getLogger().severe("Failed to save " + file.toString() + ": " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(javaPlugin);
        }
    }

    /**
     * Converts an object to a string which can be saved.
     * Uses the specified {@link PersistType}.
     * Might be empty if an {@link IOException} occurs.
     *
     * @param instance The instance which should be converted
     * @return The string representation of the object.
     */
    public String toString(Object instance) {
        try {
            return objectMapper.writeValueAsString(instance);
        } catch (IOException e) {
            javaPlugin.getLogger().severe("Failed to save " + instance.toString() + ": " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(javaPlugin);
        }
        return "";
    }

    // LOAD BY CLASS

    /**
     * Creates a new instance of a class and deserializes it automatically from the corresponding file.
     * Returns the new instance.
     *
     * @param clazz The class which should be loaded
     * @param <T>   The type of class
     * @return The loaded class. Might be null
     */
    public <T> T load(Class<T> clazz) {
        return load(clazz, getFile(clazz));
    }

    /**
     * Creates a new instance of a class and deserializes the provided file content.
     * Returns the new instance.
     *
     * @param clazz The class which should be loaded
     * @param file  The file where the class should be loaded from
     * @param <T>   The type of class
     * @return The loaded class. Might be null
     */
    public <T> T load(Class<T> clazz, File file) {
        if (file.exists()) {
            javaPlugin.getLogger().warning("one");
            try {
                javaPlugin.getLogger().warning("two");
                return objectMapper.readValue(file, clazz);
            } catch (IOException e) {
                javaPlugin.getLogger().warning("three");
                javaPlugin.getLogger().severe("Failed to parse " + file + ": " + e.getMessage());
                javaPlugin.getLogger().severe("Creating a backup for " + file.getName() + " in \"backups\" folder...");

                String backupFolderName = "backups";
                File pluginFolder = new File(javaPlugin.getDataFolder().getPath());
                File backupFolder = new File(pluginFolder.getPath() + File.separator + backupFolderName);
                File backupConfigFile = getBackUpFile(file, backupFolder);


                javaPlugin.getLogger().warning("four");
                try {
                    if (!backupFolder.exists()) backupFolder.mkdir();
                    Files.copy(file.toPath(), backupConfigFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(file.toPath());
                    javaPlugin.getLogger().info("Success! Backup \"" + file.getName() + "\" created, check \"" + backupFolder.getPath() + "\".");
                    javaPlugin.getLogger().warning("five");
                } catch (IOException exception) {

                    javaPlugin.getLogger().warning("six");
                    javaPlugin.getLogger().severe(
                            "Failed to move " + file + " to "

                                    + javaPlugin.getDataFolder().getName() + File.separator + "backups: "
                                    + exception.getMessage());
                    Bukkit.getPluginManager().disablePlugin(javaPlugin);
                    javaPlugin.getLogger().severe(e.getMessage());
                }

                javaPlugin.getLogger().warning("seven");
                // load(clazz, file);
            }
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getBackUpFile(File file, File backupFolder) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        String datevalue = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR) + "_" + calendar.get(Calendar.HOUR) + "h " + calendar.get(Calendar.MINUTE) + "m " + calendar.get(Calendar.SECOND) + "s";
        return new File(backupFolder + File.separator + file.getName().replace(".yml","") + "_" + datevalue + persistType.getExtension());
    }

    /**
     * Creates a new instance of a class and deserializes the provided content.
     * Returns the new instance.
     *
     * @param clazz   The class which should be loaded
     * @param content The content which should be loaded into the class
     * @param <T>     The type of class
     * @return The loaded class. Might be null
     */
    public <T> T load(Class < T > clazz, String content) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(javaPlugin);
        }

        return null;
    }

    /**
     * Represents a data format which can be
     * used for persisting.
     */
    public enum PersistType {

        YAML(".yml", new YAMLFactory()),
        JSON(".json", new JsonFactory());

        private final String extension;
        private final JsonFactory factory;

        /**
         * The default constructor.
         *
         * @param extension The file extension with a leading full stop
         * @param factory   The JsonFactory which should be used for persisting
         */
        PersistType(String extension, JsonFactory factory) {
            this.extension = extension;
            this.factory = factory;
        }

        /**
         * The file extension used for persisting configuration files.
         * Starts with a leading dot.
         *
         * @return The extension of the file
         */
        public String getExtension() {
            return extension;
        }

        /**
         * Returns the {@link JsonFactory} used by Jackson for serialization and deserialization.
         *
         * @return The {@link JsonFactory} for Jackson
         */
        public JsonFactory getFactory() {
            return factory;
        }
    }
}
