package myclass.entityboss;

import myclass.entityboss.command.MainCommand;
import myclass.entityboss.command.SpawnBoss;
import myclass.entityboss.listener.boss.DragonBoss;
import myclass.entityboss.listener.boss.ZombieBoss;
import myclass.entityboss.listener.menu.BossMenu;
import myclass.entityboss.listener.player.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import us.narwell.narapi.bukkit.configuration.FileBuilder;
import us.narwell.narapi.bukkit.util.Colorize;

public class EntityBossAPI {

    private static EntityBossAPI instance;
    private final EntityBoss main;

    // variables
    private FileBuilder conf;
    private FileBuilder zombie;
    private FileBuilder lang;


    // constructor
    public EntityBossAPI(final EntityBoss plugin) {

        instance = this;
        main = plugin;

        conf = new FileBuilder(plugin, "config");
        lang = new FileBuilder(plugin, "lang");
        zombie = new FileBuilder(plugin, "bosses", "zombie");

        loadCommand(plugin);
        loadListener(plugin);

    }


    private void loadListener(final EntityBoss main) {
        PluginManager pluginManager = Bukkit.getPluginManager();

        /* Global Listeners */
        pluginManager.registerEvents(new DragonBoss(), main);
        pluginManager.registerEvents(new ZombieBoss(), main);
        pluginManager.registerEvents(new PlayerListener(), main);
        pluginManager.registerEvents(new BossMenu(), main);


        Bukkit.getConsoleSender().sendMessage(Colorize.set("&8(&eEntityBoss&8)  &aListener Loaded..."));

    }

    private void loadCommand(final EntityBoss main) {

        main.getCommand("boss").setExecutor(new SpawnBoss());
        main.getCommand("test").setExecutor(new MainCommand());

        Bukkit.getConsoleSender().sendMessage(Colorize.set("&8(&eEntityBoss&8)  &aCommands Loaded..."));

    }

    public EntityBoss getMain() {
        return main;
    }

    public FileBuilder getConf() {
        return conf;
    }

    public FileBuilder getZombie() {
        return zombie;
    }
    public FileBuilder getLang(){
        return lang;
    }

    public static EntityBossAPI getInstance() {
        return instance;
    }




}
