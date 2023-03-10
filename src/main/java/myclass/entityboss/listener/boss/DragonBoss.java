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
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import us.narwell.narapi.bukkit.util.Colorize;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DragonBoss implements Listener {

    private ProtocolManager pm;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof EnderDragon) {
            Player player = (Player) event.getEntity();
            event.setDamage(100.0);
            // 48.0
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {

        pm = ProtocolLibrary.getProtocolManager();

        if (event.getEntity() instanceof EnderDragon) {
            Player killer = event.getEntity().getKiller();
            if (killer != null) {

                String sendMessage = EntityBossAPI.getInstance().getConf().getString("CHAT.DRAGON.DEATH-MESSAGE");
                String replacedMessage1 = PlaceholderAPI.setPlaceholders(event.getEntity().getKiller(), Colorize.set(sendMessage));
                replacedMessage1 = replacedMessage1.replaceAll("<killer_dragon>", killer.getName());

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(replacedMessage1);
                }

                if (EntityBossAPI.getInstance().getConf().getString("REWARD.ACTIVE").equals("true")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), EntityBossAPI.getInstance().getConf().getString("REWARD.COMMAND") + " " + killer.getName());

                    killer.sendMessage(Colorize.set(EntityBossAPI.getInstance().getConf().getString("CHAT.REWARD.MESSAGE")));
                } else {


                    List<Player> players1 = new ArrayList<>(Bukkit.getOnlinePlayers());
                    for (int i = 0; i < players1.size(); i++) {
                        Player player2 = players1.get(i);
                        TitleAPI.sendTitle(player2, 10, 30, 10, EntityBossAPI.getInstance().getConf().getString("TITLES.END-EVENT.TITLE"), EntityBossAPI.getInstance().getConf().getString("TITLES.END-EVENT.SUBTITLE"));
                    }
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

                // Cancela la tarea despu??s de 10 segundos
                Bukkit.getScheduler().runTaskLater(EntityBossAPI.getInstance().getMain(), () -> Bukkit.getScheduler().cancelTask(taskId), 200L);
            }
        } else if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            EntityDamageEvent damageEvent = player.getLastDamageCause();
            if (damageEvent != null && damageEvent instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) damageEvent).getDamager();
                if (damager instanceof EnderDragon) {

                    String sendMessage2 = EntityBossAPI.getInstance().getConf().getString("CHAT.DRAGON.PLAYER-DEATH");
                    String replacedMessage2 = PlaceholderAPI.setPlaceholders(event.getEntity().getKiller(), Colorize.set(sendMessage2));
                    replacedMessage2 = replacedMessage2.replaceAll("<death-killer>", player.getName());

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.sendMessage(replacedMessage2);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (EntityBossAPI.getInstance().getConf().getString("EVENTS.DRAGON-DESTROY-BLOCKS").equals("true")) {
            if (event.getEntity() instanceof EnderDragon) {
                event.blockList().clear();
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (EntityBossAPI.getInstance().getConf().getString("EVENTS.BREAK-EGG").equals("true")) {

            if (event.getBlock().getType() == Material.DRAGON_EGG) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (EntityBossAPI.getInstance().getConf().getString("EVENTS.PLACE-EGG").equals("true")) {
            if (event.getBlock().getType() == Material.DRAGON_EGG) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (EntityBossAPI.getInstance().getConf().getString("EVENTS.INTERACT-EGG").equals("true")) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DRAGON_EGG) {
                event.setCancelled(true);
            }
        }
        World world = player.getWorld();
        if (world.getName().equals("BossEnd") && event.hasItem() && event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == (short) 1) {
            event.setCancelled(true);
            player.sendMessage(Colorize.set("La manzana de Notch est?? desactivada en este mundo."));
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }
}