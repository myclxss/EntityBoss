package myclass.entityboss.accesories;

import myclass.entityboss.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileAPI {

    public static File configFile;
    public static FileConfiguration config;

    public static File zombieFile;
    public static FileConfiguration zombie;

    public static void base(Main main) {
        if (!main.getDataFolder().exists()) {
            main.getDataFolder().mkdirs();
        }
        configFile = new File(main.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            main.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        zombieFile = new File(main.getDataFolder(), "zombie.yml");
        if (!zombieFile.exists()) {
            main.saveResource("zombie.yml", false);
        }
        zombie = YamlConfiguration.loadConfiguration(zombieFile);
    }
}
