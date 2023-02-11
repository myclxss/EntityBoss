package myclass.entityboss.listener;

import myclass.entityboss.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DragonBoss implements Listener {
    private Main plugin;

    public DragonBoss(Main main) {
        this.plugin = main;
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof EnderDragon) {
            Player player = (Player) event.getEntity();
            event.setDamage(1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof EnderDragon) {
            Player killer = e.getEntity().getKiller();
            if (killer != null) {
                // Otorgar recompensa al jugador que mató al dragón aquí.
                // Por ejemplo, puedes darle un item:
                ItemStack reward = new ItemStack(Material.DIAMOND);
                killer.getInventory().addItem(reward);
                killer.sendMessage("Has ganado un diamante por matar al dragón!");
                Bukkit.broadcastMessage(killer.getName() + " ha matado al dragón!");
                Bukkit.broadcastMessage("¡El dragón ha sido derrotado por " + killer.getName() + "!");
            }
        } else if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            EntityDamageEvent damageEvent = player.getLastDamageCause();
            if (damageEvent != null && damageEvent instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) damageEvent).getDamager();
                if (damager instanceof EnderDragon) {
                    Bukkit.broadcastMessage(player.getName() + " ha sido asesinado por el End Dragon");
                }
            }
        }
    }
}