package myclass.entityboss;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.narwell.narapi.bukkit.util.Colorize;

public class EntityBoss extends JavaPlugin {

    // Variable
    private EntityBossAPI api;


    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage(Colorize.set("&e════════════════════════════════════════════════════"));
        Bukkit.getConsoleSender().sendMessage(Colorize.set("&8(&eEntityBoss&8) --> &aby myclass &8--> &aOnline"));
        Bukkit.getConsoleSender().sendMessage(Colorize.set("&e════════════════════════════════════════════════════"));

        api = new EntityBossAPI(this);

    }


    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(Colorize.set("&e════════════════════════════════════════════════════"));
        Bukkit.getConsoleSender().sendMessage(Colorize.set("&8(&eEntityBoss&8) --> &aby myclass &8--> &cOffline"));
        Bukkit.getConsoleSender().sendMessage(Colorize.set("&e════════════════════════════════════════════════════"));

    }
}
