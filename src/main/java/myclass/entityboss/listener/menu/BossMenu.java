package myclass.entityboss.listener.menu;

import myclass.entityboss.EntityBossAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BossMenu implements Listener {

    public void createBossMenu(Player player){
        Inventory inventory = Bukkit.createInventory(null, 27, (EntityBossAPI.getInstance().getLang().getString("MENU.SPAWN-BOSS.NAME", true)));

        ItemStack zombieBoss = new ItemStack(383, 1, (short) 54);
        ItemMeta zombieBossMeta = zombieBoss.getItemMeta();
        zombieBossMeta.setDisplayName(EntityBossAPI.getInstance().getLang().getString("MENU.SPAWN-BOSS.ITEMS.ZOMBIE-BOSS.NAME", true));
        List<String> zombieBossLore = EntityBossAPI.getInstance().getLang().getStringList("MENU.SPAWN-BOSS.ITEMS.ZOMBIE-BOSS.LORE");
        zombieBossMeta.setLore(zombieBossLore);
        zombieBoss.setItemMeta(zombieBossMeta);
        inventory.setItem(EntityBossAPI.getInstance().getLang().getInt("MENU.SPAWN-BOSS.ITEMS.ZOMBIE-BOSS.SLOT"), zombieBoss);

        player.openInventory(inventory);
    }
    @EventHandler
    public void clickInventory(InventoryClickEvent event){
        if (event.getView().getTitle().equals(EntityBossAPI.getInstance().getLang().getString("MENU.SPAWN-BOSS.NAME", true))){
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR){
                event.setCancelled(true);
            }
            if (!event.getCurrentItem().hasItemMeta()){
                event.setCancelled(true);
            }
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);

            if (event.getSlot() == EntityBossAPI.getInstance().getLang().getInt("MENU.SPAWN-BOSS.ITEMS.ZOMBIE-BOSS.SLOT")){

            }
        }
    }
}
