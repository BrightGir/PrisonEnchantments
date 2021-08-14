package me.bright.enchantments;

import me.bright.enums.PEnchantment;
import me.bright.util.Chance;
import me.bright.util.PELogger;
import me.bright.util.PEManager;
import me.bright.utils.CoolDown;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DamageList implements Listener {

    private Set<Entity> entites = new HashSet<>();
    private Set<Entity> torEntites = new HashSet<>();


    //МЕЧ

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) {
            return;
        }
        if(event.isCancelled()) {
            return;
        }
        if(event.getDamager() == event.getEntity()) {
            event.setCancelled(true);
            return;
        }

        Entity entity = event.getEntity();
        Player damager = (Player) event.getDamager();
        ItemStack mainHand = damager.getInventory().getItemInMainHand();
        HashSet<PEnchantment> enchantments = PEManager.getEnchantments(mainHand);
        HashMap<PEnchantment,Integer> currentEnchantments = PEManager.getEnchantmentsMap(mainHand);

        if(enchantments.contains(PEnchantment.LIFESTEAL)) {//LIFESTEAL
            if(Chance.isLuck(25)) {
                double maxHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                if (damager.getHealth() + 1 > maxHealth) {
                    damager.setHealth(maxHealth);
                } else {
                    damager.setHealth(damager.getHealth() + 1);
                }
            }
        }


        if(enchantments.contains(PEnchantment.RAZOR)) {
            if(Chance.isLuck(20)) {
                int sum = PEManager.getSumOfLevel(mainHand,PEnchantment.RAZOR); //RAZOR
                event.setDamage(event.getDamage() + sum);
            }
        }

        if(enchantments.contains(PEnchantment.THROW)) {  //THROW
            if(Chance.isLuck(15)) {
                int lvl = currentEnchantments.get(PEnchantment.THROW);
                Vector vec = damager.getLocation().getDirection();
                vec.setY(vec.getY() + (lvl > 1 ? 1.8 : 1.5)); // 2.2
                entity.setVelocity(vec);
            }
        }

        if(enchantments.contains(PEnchantment.TOR)) { //TOR (МОЛНИЯ)
            if(Chance.isLuck(15)) {
                damager.getWorld().strikeLightning(entity.getLocation());
            }
        }

        if(entites.isEmpty() && enchantments.contains(PEnchantment.COVERAGE)) {      // ОХВАТ
            if(Chance.isLuck(15)) {
                int lvl = currentEnchantments.get(PEnchantment.COVERAGE);
                for(Entity en : entity.getNearbyEntities(lvl,1,lvl)) {
                    if(en instanceof Damageable) {
                        ((Damageable) en).damage(event.getDamage(),damager);
                    }
                }
                entites.clear();
            }
        }


        //МЕЧ КОГДА НУЖЕН ДАМАГЕР ИГРОК
        if(entity instanceof Player) {

            // PLAYER - ЖЕРТВА, mainHAND - У ДАМАГЕРА
            Player player = (Player) entity;
            if(enchantments.contains(PEnchantment.FOCUS)) { //FOCUS
                if(Chance.isLuck(15)) {
                    Vector direction = player.getLocation().getDirection().normalize();
                    direction.multiply(-2);
                    damager.teleport(player.getLocation().add(direction));
                }
            }

            if(enchantments.contains(PEnchantment.ILLUSION)) { // ILLUSION
                if(Chance.isLuck(15)) {
                    int lvl = currentEnchantments.get(PEnchantment.ILLUSION);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,80,lvl-1,false,false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,80,lvl-1,false,false));
                }
            }

            if(enchantments.contains(PEnchantment.TOXIC)) {        // TOXIC
                if(Chance.isLuck(10)) {
                    int lvl = currentEnchantments.get(PEnchantment.TOXIC);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,30 * lvl,(lvl == 3 ? 1 : 0),false,false));
                }
            }


        }
    }

    //БРОНЯ
    @EventHandler
    public void onDmg(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player)event.getEntity();
        for(ItemStack item : p.getInventory().getArmorContents()) {
            if(item != null && item.getType() != Material.AIR) {

                HashSet<PEnchantment> enchantments = PEManager.getEnchantments(item);
                HashMap<PEnchantment,Integer> currentEnchantments = PEManager.getEnchantmentsMap(item);

                //КОГДА НУЖНА ТОЛЬКО ЖЕРТВА
                if(event.getCause() == EntityDamageEvent.DamageCause.FALL && enchantments.contains(PEnchantment.GOD)) { // GOD
                    event.setDamage(0);
                    return;
                }

                if(enchantments.contains(PEnchantment.SURVIVAL)) { //SURVIVAL (САМОСОХРАНЕНИЕ)
                    if(Chance.isLuck(5)) {
                        int lvl = currentEnchantments.get(PEnchantment.SURVIVAL);
                        double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                        if(p.getHealth() + (2 * lvl) >= maxHealth) {
                            p.setHealth(maxHealth);
                        } else {
                            p.setHealth(p.getHealth() + (2 * lvl));
                        }
                    }
                }

                if (p.getHealth() <= 7.0D && enchantments.contains(PEnchantment.ANIMAL) && CoolDown.isOutDated(p,"animal")) { // ANIMAL
                    int lvl = currentEnchantments.get(PEnchantment.ANIMAL);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,20,lvl-1,false,false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,120,lvl-1,false,false));
                    new CoolDown(p,"animal",60,true);
                }
            }
        }

        if(event instanceof EntityDamageByEntityEvent) {
            Entity damagerEntity = ((EntityDamageByEntityEvent) event).getDamager();
            for (ItemStack item : p.getInventory().getArmorContents()) {

                HashSet<PEnchantment> enchantments = PEManager.getEnchantments(item);
                HashMap<PEnchantment,Integer> currentEnchantments = PEManager.getEnchantmentsMap(item);
                //БРОНЯ КОГДА НУЖЕН ЛЮБОЙ ДАМАГЕР
                if(enchantments.contains(PEnchantment.TANK)) {
                    int lvl = currentEnchantments.get(PEnchantment.TANK);
                    event.setDamage(getDamagePercent(event.getDamage(), 100 - (lvl * 10)));
                }

                if(enchantments.contains(PEnchantment.LAVA)) {
                    if(Chance.isLuck(20)) {
                        int lvl = currentEnchantments.get(PEnchantment.LAVA);
                        damagerEntity.setFireTicks(40 * lvl);
                    }
                }

                //БРОНЯ КОГДА НУЖЕН ИГРОК ДАМАГЕР
                if(damagerEntity instanceof Player) {

                    Player damager = (Player) damagerEntity;

                    if (enchantments.contains( PEnchantment.DOMINATE)) { //DOMINATE
                        int lvl = currentEnchantments.get(PEnchantment.DOMINATE);
                        if (Chance.isLuck(10)) {
                            damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * lvl, (lvl > 1 ? 1 : 0), false, false));
                            damager.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30 * lvl, (lvl > 1 ? 1 : 0), false, false));
                            return;
                        }
                    }

                    if (enchantments.contains(PEnchantment.REFLECTION)) { //REFLECTION
                        int lvl = currentEnchantments.get(PEnchantment.REFLECTION);
                        if (Chance.isLuck(5 * lvl)) {
                            damager.damage(event.getDamage(), p);
                            event.setDamage(0);
                            return;
                        }
                    }
                }
            }
        }
    }
    // p - жертва, damager - дамагер


    public double getDamagePercent(double damage, double percent) {
        return (damage/100)*percent;
    }
}
