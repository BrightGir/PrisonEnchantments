package me.bright.listeners;

import me.bright.enums.PEnchantment;
import me.bright.util.Chance;
import me.bright.util.M;
import me.bright.util.PEManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PEListener implements Listener {

    @EventHandler
    public void onEnchantment(InventoryClickEvent event) {
        if(!(event.isLeftClick())) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCursor();
        if(item.getType() == Material.ENCHANTED_BOOK) {

            ItemStack clicked = event.getCurrentItem();

            if(PEManager.getEnchantmentsSize(clicked) >= 2) {
                M.msg(player,"&aНа этот предмет наложено максимальное количество зачарований");
                return;
            }

            try {

                PEnchantment ench = PEManager.getEnchantmentInBook(item);
                if(!(ench.getType().getMaterials().contains(clicked.getType()))) {
                    return;
                }
                Material type = clicked.getType();
                if(ench == PEnchantment.TRENCH && type.toString().endsWith("_SPADE")) {
                    M.msg(player,"&cВы не можете наложить это зачарование на лопату!");
                    return;
                }

                ItemMeta meta = item.getItemMeta();
                int lvl = ench.getLevelOfBook(item);
                int chance = PEManager.getChance(meta.getLore());

                if(Chance.getRandomNumber(1,100) <= chance) {
                    PEManager.addEnchantment(event.getCurrentItem(), ench,lvl);
                    player.sendTitle(M.colored("&aВы наложили зачарование!"),"",10,15,10);
                    M.msg(player,"&aЗачарование наложено!");
                } else {
                    player.sendTitle((M.colored("&cНеудача!")),"",10,15,10);
                    M.msg(player,"&cВы потерпели неудачу!");
                }

                player.setItemOnCursor(null);
                player.updateInventory();
                event.setCancelled(true);

            } catch (Exception ignored) {

            }

        }
    }
}
