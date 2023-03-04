package myclass.entityboss.listener.boss;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.clip.placeholderapi.PlaceholderAPI;
import myclass.entityboss.EntityBossAPI;
import myclass.entityboss.accesories.TitleAPI;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import us.narwell.narapi.bukkit.util.Colorize;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ZombieBoss implements Listener {

    private ProtocolManager pm;

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.hasMetadata("ZombieBoss")) {
                String name = Colorize.set(EntityBossAPI.getInstance().getZombie().getString("ZOMBIE-BOSS.NAME")) + ChatColor.GREEN + "(" + formatHealth(zombie.getHealth()) + "/" + formatHealth(zombie.getMaxHealth()) + ")";
                zombie.setCustomName(name);
                zombie.setCustomNameVisible(true);
                zombie.setMaxHealth(90.0);
                zombie.setHealth(90.0);

            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.hasMetadata("ZombieBoss")) {
                String newName = Colorize.set(EntityBossAPI.getInstance().getZombie().getString("ZOMBIE-BOSS.NAME")) + ChatColor.GREEN + "(" + formatHealth(zombie.getHealth()) + "/" + formatHealth(zombie.getMaxHealth()) + ")";
                zombie.setCustomName(newName);

                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                    event.setCancelled(true);
                }
                if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                    event.setCancelled(true);
                }

                zombie.getWorld().playEffect(zombie.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getDamager();
            if (zombie.hasMetadata("ZombieBoss")) {
                Player player = (Player) event.getEntity();
                event.setDamage(300.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
            }
        }
    }

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {
        pm = ProtocolLibrary.getProtocolManager();
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.hasMetadata("ZombieBoss")) {
                Player killer = event.getEntity().getKiller();
                if (killer != null) {

                    String sendMessage = EntityBossAPI.getInstance().getZombie().getString("CHAT.ZOMBIE.DEATH-MESSAGE");
                    String replacedMessage1 = PlaceholderAPI.setPlaceholders(event.getEntity().getKiller(), Colorize.set(sendMessage));
                    replacedMessage1 = replacedMessage1.replaceAll("<killer_zombie>", killer.getName());

                    List<Player> players1 = new ArrayList<>(Bukkit.getOnlinePlayers());
                    for (int i = 0; i < players1.size(); i++) {
                        Player player2 = players1.get(i);
                        TitleAPI.sendTitle(player2, 10, 30, 10, EntityBossAPI.getInstance().getZombie().getString("TITLES.END-EVENT.TITLE"), EntityBossAPI.getInstance().getZombie().getString("TITLES.END-EVENT.SUBTITLE"));
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(replacedMessage1);
                    }
                    double radius = 0.6;
                    int taskId = new BukkitRunnable() {
                        double time = 0;

                        @Override
                        public void run() {
                            Location loc = killer.getLocation();
                            double x = radius * Math.sin(time);
                            double z = radius * Math.cos(time);
                            PacketContainer packet = pm.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                            packet.getModifier().writeDefaults();
                            packet.getParticles().write(0, EnumWrappers.Particle.FLAME);
                            packet.getFloat().write(0, (float) (loc.getX() + x)).write(1, (float) (loc.getY() + 2)).write(2, (float) (loc.getZ() + z));
                            Bukkit.getOnlinePlayers().forEach(online -> {
                                try {
                                    pm.sendServerPacket(online, packet);
                                } catch (InvocationTargetException e1) {
                                    e1.printStackTrace();
                                }
                            });
                            time += 0.15;
                        }
                    }.runTaskTimer(EntityBossAPI.getInstance().getMain(), 0, 1).getTaskId();

                    // Cancela la tarea después de 10 segundos
                    Bukkit.getScheduler().runTaskLater(EntityBossAPI.getInstance().getMain(), () -> Bukkit.getScheduler().cancelTask(taskId), 200L);
                }
                if (EntityBossAPI.getInstance().getConf().getString("REWARD.ACTIVE").equals("true")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), EntityBossAPI.getInstance().getZombie().getString("REWARD.COMMAND") + " " + killer.getName());

                    killer.sendMessage(EntityBossAPI.getInstance().getZombie().getString("CHAT.REWARD.MESSAGE", true));
                }
            }
        } else if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            EntityDamageEvent damageEvent = player.getLastDamageCause();
            if (damageEvent != null && damageEvent instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) damageEvent).getDamager();
                if (damager instanceof Zombie) {

                    String sendMessage2 = EntityBossAPI.getInstance().getZombie().getString("CHAT.ZOMBIE.PLAYER-DEATH");
                    String replacedMessage2 = PlaceholderAPI.setPlaceholders(event.getEntity().getKiller(), Colorize.set(sendMessage2));
                    replacedMessage2 = replacedMessage2.replaceAll("<death-killer>", player.getName());

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.sendMessage(replacedMessage2);
                    }
                }
            }
        }
    }

    private String formatHealth(double health) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(health);
    }
}
