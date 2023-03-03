package myclass.entityboss.command;

import me.clip.placeholderapi.libs.kyori.adventure.bossbar.BossBar;
import me.clip.placeholderapi.libs.kyori.adventure.text.Component;
import me.clip.placeholderapi.libs.kyori.adventure.text.format.NamedTextColor;
import myclass.entityboss.Main;
import myclass.entityboss.accesories.FileAPI;
import myclass.entityboss.accesories.TitleAPI;
import myclass.entityboss.accesories.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;


public class SpawnBoss implements CommandExecutor{

    private final Main plugin;

    public SpawnBoss(Main main) {
        this.plugin = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(Utils.color("&7&m-----------------------------------"));
            player.sendMessage(Utils.color("&r"));
            player.sendMessage(Utils.color("&8» &6Plugin: &fEntityBoss"));
            player.sendMessage(Utils.color("&8» &6Developer: &fmyclass"));
            player.sendMessage(Utils.color("&r"));
            player.sendMessage(Utils.color("&7&m-----------------------------------"));
            return true;
        }

        if (!args[0].equalsIgnoreCase("spawn")) {
            player.sendMessage(Utils.color("&eUsage: &8/boss spawn dragon"));
            return true;
        }
        if (args[1].equalsIgnoreCase("dragon")) {
            if (player.hasPermission("entityboss.spawn.dragon")) {
                EnderDragon enderDragon = player.getWorld().spawn(player.getLocation().add(0.5, 0, 0.5), EnderDragon.class);
                enderDragon.setCustomName(Utils.color(FileAPI.config.getString("CONFIG.DRAGON-NAME")));
                enderDragon.setCustomNameVisible(true);
                enderDragon.setHealth(200);
                enderDragon.setMaxHealth(200);
                enderDragon.setMetadata("DragonBoss", new FixedMetadataValue(plugin, "DragonBoss"));

                // Agregar armadura
                PotionEffect armorEffect = new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 4, true, false);
                enderDragon.addPotionEffect(armorEffect);

                // Agrega la regeneración de vida del dragón
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (enderDragon.isDead()) {
                            cancel();
                            return;
                        }
                        enderDragon.setHealth(Math.min(enderDragon.getHealth() + 45, enderDragon.getMaxHealth()));
                    }
                }.runTaskTimer(plugin, 0, 500); // 25 segundos = 500 ticks

                List<Player> players1 = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (int i = 0; i < players1.size(); i++) {
                    Player player2 = players1.get(i);
                    TitleAPI.sendTitle(player2, 10, 30, 10, FileAPI.config.getString("TITLES.SPAWN-DRAGON.TITLE"), FileAPI.config.getString("TITLES.SPAWN-DRAGON.SUBTITLE"));
                }
                return true;
            }
        }
        if (args[1].equalsIgnoreCase("zombie")){
            if (player.hasPermission("entityboss.spawn.zombie")){
                Zombie zombie = player.getWorld().spawn(player.getLocation().add(0.5, 0, 0.5), Zombie.class);

                zombie.setCustomName(Utils.color(FileAPI.zombie.getString("ZOMBIE-BOSS.NAME")));
                zombie.setCustomNameVisible(true);
                zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                zombie.setMetadata("ZombieBoss", new FixedMetadataValue(plugin, "ZombieBoss"));
            }
        }
        return false;
    }
}
