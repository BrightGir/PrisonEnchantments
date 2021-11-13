package me.bright.enchantments;

import me.bright.PEnchantments;
import me.bright.enums.PEnchantment;
import me.bright.util.Chance;
import me.bright.util.M;
import me.bright.util.PEManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;

public class Bow implements Listener {

    private HashSet<Player> frozenEntities = new HashSet<>();



    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        ItemStack item = event.getBow();

        HashSet<PEnchantment> enchantments = PEManager.getEnchantments(item);
        HashMap<PEnchantment,Integer> currentEnchantments = PEManager.getEnchantmentsMap(item);

        if(enchantments.contains(PEnchantment.FIREBALL)) {
            Location fireballloc = player.getEyeLocation().toVector().add(player.getEyeLocation().getDirection()).toLocation(player.getWorld());
            fireballloc.setDirection(player.getLocation().getDirection().multiply(6));
            Entity fire = player.getWorld().spawnEntity(fireballloc, EntityType.FIREBALL);
            event.setProjectile(fire);
            return;
        }

        if(enchantments.contains(PEnchantment.ARSON)) {
            event.getProjectile().setFireTicks(1000);
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if(e.getEntity().getShooter() instanceof Player && e.getHitEntity() instanceof Player) {
            Player player = (Player) e.getEntity().getShooter();
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            HashSet<PEnchantment> enchantments = PEManager.getEnchantments(mainHand);
            HashMap<PEnchantment,Integer> currentEnchantments = PEManager.getEnchantmentsMap(mainHand);
            if(enchantments.contains(PEnchantment.FROZEN)) {
                if(Chance.isLuck(20)) {
                    Player frozen = (Player) e.getHitEntity();
                    int lvl = currentEnchantments.get(PEnchantment.FROZEN);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            frozenEntities.remove(frozen);
                        }
                    }.runTaskLater(PEnchantments.getPlugin(),30 * lvl);
                }
            }
        }
        e.getEntity().remove();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(frozenEntities.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }


}
