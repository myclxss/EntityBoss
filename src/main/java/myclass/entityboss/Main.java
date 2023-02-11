package myclass.entityboss;

import myclass.entityboss.accesories.Utils;
import myclass.entityboss.listener.DragonBoss;
import myclass.entityboss.listener.SpawnBoss;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static Main instance;

    private List<UUID> rainbowPlayers = new ArrayList<>();

    @Override
    public void onEnable() {

        Utils.log("&6SuitCosmetics &7- &aOnline");

        Main.instance = this;

        /* Loaded Class */
        loadListener();
        loadCommand();

    }

    @Override
    public void onDisable() {

        Utils.log("&6SuitCosmetics &7- &cOffline");

    }

    public void loadListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        /* Global Listeners */
        pluginManager.registerEvents(new DragonBoss(this), this);

        Utils.log("&7(&6SuitCosmetics&7) &aListener Loaded...");

    }

    public void loadCommand() {

        getCommand("boss").setExecutor(new SpawnBoss(this));

        Utils.log("&7(&6SuitCosmetics&7) &aCommands Loaded...");

    }

    public static Main getInstance() {
        return Main.instance;
    }
}
