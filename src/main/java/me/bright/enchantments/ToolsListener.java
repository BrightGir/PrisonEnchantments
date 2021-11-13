package me.bright.enchantments;

import me.bright.PEnchantments;
import me.bright.Prison;
import me.bright.ViPlayer;
import me.bright.enums.PEnchantment;
import me.bright.util.Break;
import me.bright.util.Chance;
import me.bright.util.M;
import me.bright.util.PEManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Tools implements Listener {

    private HashSet<UUID> booms = new HashSet<>(); // место где хранятся игроки у которых прокнул бум чтобы блокбрейкевент не мог вызваться дважды



    @EventHandler
    public void onTool(BlockBreakEvent event) {
        Material block = event.getBlock().getType();

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        ViPlayer player = ViPlayer.getViPlayer(event.getPlayer());
        if(item == null || item.getType() == Material.AIR) {
            return;
        }
        HashSet<PEnchantment> enchantments = PEManager.getEnchantments(item);
        HashMap<PEnchantment,Integer> currentEnchantments = PEManager.getEnchantmentsMap(item);

        if(enchantments.contains(PEnchantment.MONEY_BAG)) {                           // MONEY BAG
            int size = PEManager.getNumberOfSpecifyEnchantment(item,PEnchantment.MONEY_BAG);
            int chance = (size > 1 ? 30 : 15);
            if(Chance.isLuck(chance)) {
                player.addMoney(Prison.getPrison().getBlocksPrice().get(block) * 3 * (1 * player.getMoneyMultiplier()));
            }
        }

        if(enchantments.contains(PEnchantment.MINER)) {                                      // MINER
            if(Chance.isLuck(5) && !player.wrap().hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                int lvl = currentEnchantments.get(PEnchantment.MINER);
                Player p = player.wrap();
                p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,120,lvl-1,false,false));
                if(!(p.hasPotionEffect(PotionEffectType.SPEED))) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,120,lvl-1,false,false));
                }
            }
        }
                                                                                                      // БУР
        if(!(booms.contains(player.wrap().getUniqueId())) && enchantments.contains(PEnchantment.TRENCH)) {
            if(Chance.isLuck(10)) {
                booms.add(player.wrap().getUniqueId());
                breakBlocks(player.wrap(), event.getBlock());
                return;
            }
        }



    }

    //3X3X1 ЛОМАНИЕ БЛОКОВ

    public void breakBlocks(Player p, Block block) {
        BlockFace face = Break.getBlockFace(p);
        ViPlayer player = new ViPlayer(p);
        Bukkit.getPluginManager().callEvent(new BlockBreakEvent(block,p));
        Break.getSquareRotation(block.getLocation(),face).stream().filter(b -> b.getType() != Material.AIR && b.getType() != Material.OBSIDIAN && b.getType() != Material.BEDROCK).forEach(b -> {
            Bukkit.getPluginManager().callEvent(new BlockBreakEvent(b,p));
        });
        booms.remove(p.getUniqueId());
    }


    public List<Block> getBlocks(Block block) {
        List<Block> blocks = new ArrayList<>();
        int bx = (int) block.getLocation().getX();
        int by = (int) block.getLocation().getY();
        int bz = (int) block.getLocation().getZ();
        World world = block.getWorld();

        for (int x =  bx - 2; x <= bx + 2; x++) {
            for (int y = by - 2; y <= by + 2; y++) {
                for (int z = bz - 2; z <= bz + 2; z++) {
                    blocks.add(new Location(world, (double) x, (double) y, (double) z).getBlock());
                }
            }
        }
        return blocks;
    }
}
