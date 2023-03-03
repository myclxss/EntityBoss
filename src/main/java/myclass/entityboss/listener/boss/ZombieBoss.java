package myclass.entityboss.listener.boss;

import myclass.entityboss.accesories.FileAPI;
import myclass.entityboss.accesories.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;

public class ZombieBoss implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.hasMetadata("ZombieBoss")) {
                String name = Utils.color(FileAPI.zombie.getString("ZOMBIE-BOSS.NAME")) + ChatColor.GREEN + "(" + formatHealth(zombie.getHealth()) + "/" + formatHealth(zombie.getMaxHealth()) + ")";
                zombie.setCustomName(name);
                zombie.setCustomNameVisible(true);

                zombie.setMaxHealth(20.0);
                zombie.setHealth(20.0);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.hasMetadata("ZombieBoss")) {
                String newName = Utils.color(FileAPI.zombie.getString("ZOMBIE-BOSS.NAME")) + ChatColor.GREEN + "(" + formatHealth(zombie.getHealth()) + "/" + formatHealth(zombie.getMaxHealth()) + ")";
                zombie.setCustomName(newName);
            }
        }
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getDamager();
            if (zombie.hasMetadata("ZombieBoss")) {
                Player player = (Player) event.getEntity();
                event.setDamage(100.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
            }
        }
    }

    private String formatHealth(double health) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(health);
    }
}
