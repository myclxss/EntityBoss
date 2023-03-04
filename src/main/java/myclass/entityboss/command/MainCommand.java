package myclass.entityboss.command;

import myclass.entityboss.listener.menu.BossMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player)  sender;
        if (args.length <= 0){
            player.sendMessage("command owo");
            return true;
        }
        if (args[0].equalsIgnoreCase("menu")){
            BossMenu inv = new BossMenu();
            inv.createBossMenu(player);
        }
        return false;
    }
}
