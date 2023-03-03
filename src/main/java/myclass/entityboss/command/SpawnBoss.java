package myclass.entityboss.command;

import com.comphenix.protocol.wrappers.EnumWrappers;
import me.clip.placeholderapi.libs.kyori.adventure.bossbar.BossBar;
import me.clip.placeholderapi.libs.kyori.adventure.text.Component;
import me.clip.placeholderapi.libs.kyori.adventure.text.format.NamedTextColor;
import myclass.entityboss.Main;
import myclass.entityboss.accesories.FileAPI;
import myclass.entityboss.accesories.TitleAPI;
import myclass.entityboss.accesories.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class SpawnBoss implements CommandExecutor {

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
        if (args[1].equalsIgnoreCase("zombie")) {
            if (player.hasPermission("entityboss.spawn.zombie")) {
                Zombie zombie = (Zombie) player.getWorld().spawn(player.getLocation().add(0.5, 0, 0.5), Zombie.class);

                zombie.setBaby(false);
                zombie.setVillager(false);
                String name = Utils.color(FileAPI.zombie.getString("ZOMBIE-BOSS.NAME")) + ChatColor.GREEN + "(" + formatHealth(zombie.getHealth()) + "/" + formatHealth(zombie.getMaxHealth()) + ")";
                zombie.setCustomName(name);
                zombie.setCustomNameVisible(true);
                zombie.setMetadata("ZombieBoss", new FixedMetadataValue(plugin, "ZombieBoss"));
                zombie.setMaxHealth(10.0);
                zombie.setHealth(10.0);

                PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2);
                zombie.addPotionEffect(speed, true);

                PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0);
                zombie.addPotionEffect(jump, true);

                for (int i = 0; i < 5; i++) {
                    player.getWorld().strikeLightning(zombie.getLocation());
                }

                ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
                ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);

                helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
                sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);

                zombie.getEquipment().setHelmet(helmet);
                zombie.getEquipment().setChestplate(chestplate);
                zombie.getEquipment().setLeggings(leggings);
                zombie.getEquipment().setBoots(boots);
                zombie.getEquipment().setItemInHand(sword);

                new BukkitRunnable() {
                    double t = 0;
                    int ticks = 0;

                    public void run() {
                        t += Math.PI / 8;
                        ticks++;

                        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 12) {
                            double x = t * Math.cos(theta);
                            double y = 1.5;
                            double z = t * Math.sin(theta);

                            zombie.getWorld().playEffect(new Location(zombie.getWorld(), zombie.getLocation().getX() + x, zombie.getLocation().getY() + y, zombie.getLocation().getZ() + z), Effect.MOBSPAWNER_FLAMES, 0);
                        }

                        // Detener las partículas después de 5 segundos
                        if (ticks >= 100) {
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }
        List<Player> players1 = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (int i = 0; i < players1.size(); i++) {
            Player player2 = players1.get(i);
            TitleAPI.sendTitle(player2, 10, 30, 10, FileAPI.zombie.getString("TITLES.START-EVENT.TITLE"), FileAPI.zombie.getString("TITLES.START-EVENT.SUBTITLE"));
        }
        return false;
    }

    private String formatHealth(double health) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(health);
    }
}
