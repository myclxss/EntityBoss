package myclass.entityboss;

import myclass.entityboss.accesories.FileAPI;
import myclass.entityboss.accesories.Utils;
import myclass.entityboss.command.SpawnBoss;
import myclass.entityboss.listener.boss.DragonBoss;
import myclass.entityboss.listener.boss.ZombieBoss;
import myclass.entityboss.listener.player.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {

        Utils.log("&e════════════════════════════════════════════════════");
        Utils.log("&8(&eEntityBoss&8) --> &aby myclass &8--> &aOnline");
        Utils.log("&e════════════════════════════════════════════════════");

        Main.instance = this;

        /* Files */
        saveDefaultConfig();

        /* Loaded Class */
        loadListener();
        loadCommand();
        loadFiles();

    }

    @Override
    public void onDisable() {

        Utils.log("&e════════════════════════════════════════════════════");
        Utils.log("&8(&eEntityBoss&8) --> &aby myclass &8--> &cOffline");
        Utils.log("&e════════════════════════════════════════════════════");

    }

    public void loadListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        /* Global Listeners */
        pluginManager.registerEvents(new DragonBoss(this), this);
        pluginManager.registerEvents(new ZombieBoss(), this);
        pluginManager.registerEvents(new PlayerListener(), this);

        Utils.log("&8(&eEntityBoss&8)  &aListener Loaded...");

    }

    public void loadCommand() {

        getCommand("boss").setExecutor(new SpawnBoss(this));

        Utils.log("&8(&eEntityBoss&8)  &aCommands Loaded...");

    }

    public void loadFiles() {

        FileAPI.base(this);

        Utils.log("&8(&eEntityBoss&8) &aFiles Loaded...");
    }

    public static Main getInstance() {
        return Main.instance;
    }
}
