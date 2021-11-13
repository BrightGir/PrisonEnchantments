package me.bright.enchantments;

import me.bright.PEnchantments;
import me.bright.enums.PEnchantment;
import me.bright.util.PELogger;
import me.bright.util.PEManager;
import me.bright.armor.ArmorEquipEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class Armor implements Listener {


    @EventHandler
    public void onEquip(ArmorEquipEvent event) {
        Player player = event.getPlayer();
        ItemStack oldItem = event.getOldArmorPiece();
        ItemStack newItem = event.getNewArmorPiece();


        if(oldItem != null && oldItem.getType() != Material.AIR && PEManager.hasEnchantments(oldItem)) {
            for(PEnchantment ench : PEManager.getEnchantments(oldItem)) {
                if(ench.getPottionTypes().size() != 0) {
                    ench.getPottionTypes().forEach(type -> {
                        player.removePotionEffect(type);
                    });
                }
            }

        }

        if(newItem != null && newItem.getType() != Material.AIR && PEManager.hasEnchantments(newItem)) {
            for(PEnchantment ench : PEManager.getEnchantments(newItem)) {
                if(ench.getPottionTypes().size() != 0) {
                    ench.getPottionTypes().forEach(type -> {
                        player.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, ench.getLevel(newItem)-1, true, false));
                    });
                }
            }
        }


    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        Player p = (Player)event.getEntity();
        for (ItemStack item : p.getInventory().getArmorContents()) {
            if (PEManager.hasEnchantment(item, PEnchantment.SATIETY)) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
