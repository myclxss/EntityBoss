package myclass.entityboss.listener;

import myclass.entityboss.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;


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
            player.sendMessage("uwu 1");
            return true;
        }

        if (!args[0].equalsIgnoreCase("spawn")) {
            player.sendMessage("uwu 2");
            return true;
        }
        if (args[1].equalsIgnoreCase("dragon")) {
            if (player.hasPermission("stormfreeze.admin")) {
                EnderDragon enderDragon = player.getWorld().spawn(player.getLocation().add(0.5, 0, 0.5), EnderDragon.class);
                enderDragon.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6&lEL JEFE"));
                enderDragon.setCustomNameVisible(true);
                enderDragon.setMaxHealth(2);
                enderDragon.setMetadata("SkeletonKing", new FixedMetadataValue(plugin, "skeletonking"));
            }
            return true;
        }
        return false;
    }
}
