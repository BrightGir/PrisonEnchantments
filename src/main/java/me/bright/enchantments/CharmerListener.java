package me.bright.enchantments;

import me.bright.PEnchantments;
import me.bright.enums.PEnchantment;
import me.bright.gui.GUI;
import me.bright.pevents.RemoveEnchantmentEvent;
import me.bright.util.M;
import me.bright.util.PELogger;
import me.bright.util.PEManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CharmerListener implements Listener {

    private HashMap<UUID,ItemStack> removeEnchanmentItems = new HashMap<>();
    private HashSet<ItemStack> duplicateEnchantmentsItems = new HashSet<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!(event.isLeftClick())) {
            return;
        }
        if(event.getCurrentItem() == null) {
            return;
        }
        ItemStack cursor = event.getCursor();

        ItemStack current = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        // ЗАЧАРОВАТЕЛЬ 85 ПРОЦЕНТОВ
        if(cursor.getType() == Material.END_CRYSTAL && cursor.hasItemMeta() && cursor.getItemMeta().hasLore()) {
            if(current.getType() != Material.ENCHANTED_BOOK) {
                return;
            }
            PEManager.setChance(current,85,PEManager.getChance(current.getItemMeta().getLore()));
            M.msg(player,"&aВы воспользовались заклинателем!");
            player.setItemOnCursor(null);
            event.setCancelled(true);
            return;
        }

        //ОБНУЛИТЕЛЬ
        if(cursor.getType() == Material.PRISMARINE_CRYSTALS && cursor.hasItemMeta() && cursor.getItemMeta().hasLore()) {
            if(!PEManager.hasEnchantments(current)) {
                return;
            }
            PEManager.removeEnchantments(current);
            M.msg(player,"&aВы воспользовались обнулителем!");
            player.setItemOnCursor(null);
            event.setCancelled(true);
            return;
        }

        if(cursor.getType() == Material.PRISMARINE_SHARD && cursor.hasItemMeta() && cursor.getItemMeta().hasLore()) {
            if(PEManager.getEnchantmentsSize(current) <= 1) {
                return;
            }
            GUI gui = new GUI(player,9,me.bright.utils.M.getMessage("removeEnchanment"));
            boolean similar = false;
            List<String> lore = Arrays.asList("",M.colored("&7Удалить зачарование"));
            HashMap<PEnchantment,Integer> enchantments = PEManager.getEnchantmentsMap(current);
            for(Map.Entry entry : enchantments.entrySet()) {
                if(PEManager.getNumberOfSpecifyEnchantment(current, (PEnchantment) entry.getKey()) > 1) {
                    similar = true;
                }
            }
            PEnchantment pench1;
            PEnchantment pench2;
            PEnchantment[] pEnchantments = enchantments.keySet().toArray(new PEnchantment[enchantments.size()]);
            pench1 = pEnchantments[0];
            pench2 = (similar ? pEnchantments[0] : pEnchantments[1]);
            List<Integer> levels = (similar ? PEManager.getLevelsOfSpecifyEnchantment(current,pEnchantments[0]) : Arrays.asList(enchantments.get(pench1),enchantments.get(pench2)));

            ItemStack glass = GUI.getGlass();
            ItemStack ench1 = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta ench1Meta = ench1.getItemMeta();
            ench1Meta.setDisplayName(pench1.getDisplayName() + " " + PEManager.getRomeLevelByInt(levels.get(0)));
            ench1Meta.setLore(lore);
            ItemStack ench2 = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta ench2Meta = ench2.getItemMeta();
            ench2Meta.setDisplayName(pench2.getDisplayName() + " " + PEManager.getRomeLevelByInt(levels.get(1)));
            ench2Meta.setLore(lore);

            ench1.setItemMeta(ench1Meta);
            ench2.setItemMeta(ench2Meta);
            gui.addButton(glass,0);
            gui.addButton(glass,1);
            gui.addButton(ench1,2); //
            gui.addButton(glass,3);
       //     gui.addButton(current.clone(),4);
            gui.addButton(glass,5);
            gui.addButton(ench2,6); //
            gui.addButton(glass,7);
            gui.addButton(glass,8);
            player.openInventory(gui.wrap());
            removeEnchanmentItems.put(player.getUniqueId(),current);
            event.setCancelled(true);
            player.setItemOnCursor(null);
        }

        if(cursor.getType() == Material.GOLD_NUGGET && cursor.hasItemMeta() && cursor.getItemMeta().hasLore() && cursor.getAmount() == 1) {
            if(current.getType() != Material.ENCHANTED_BOOK) {
                return;
            }
            try {
                ItemStack book = current;
                int chance = PEManager.getChance(book.getItemMeta().getLore());
                if(chance >= 70) {
                    M.msg(player,"&cМаксимальное поднятие шанса пыльцой - 70%!");
                    return;
                }
                int finalChance = Math.min(chance + 10, 70);
                PEManager.setChance(book, finalChance, chance);
                ItemStack cursorFinal = cursor.clone();
                cursorFinal.setAmount(cursor.getAmount() - 1);
                player.setItemOnCursor(cursorFinal);
                event.setCancelled(true);
                M.msg(player,"&aВы успешно воспользовались пыльцой");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onRemoveEnchantmentInventory(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if(inv != null && inv.getName() != null && inv.getName().equals(me.bright.utils.M.getMessage("removeEnchanment"))) {
            if(event.getSlot() == 2 || event.getSlot() == 6) {
              Player player = (Player) event.getWhoClicked();
              String enchName = event.getCurrentItem().getItemMeta().getDisplayName();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        PEManager.removeEnchantmentByString(player,removeEnchanmentItems.get(player.getUniqueId()),enchName);
                    }
                }.runTaskLaterAsynchronously(PEnchantments.getPlugin(),10);
             //   Bukkit.getPluginManager().callEvent(removeEvent);
                M.msg(player, "&aВы успешно удалили зачарование");
                player.closeInventory();
            }
        }
    }


    @EventHandler
    public void onClose(InventoryCloseEvent event) {

    }

    public int getSlot(PlayerInventory inv, ItemStack item) {
        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) != null && inv.getItem(i).isSimilar(item)) {
                return i;
            }
        }
        return -100;
    }



}
