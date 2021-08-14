package me.bright.util;

import me.bright.PEnchantments;
import me.bright.enums.PEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class EnchChest implements Listener {

    private static HashSet<ItemStack> books;
    private static ItemStack[] booksArray;
    private static Random random = new Random();
    private static HashMap<UUID,ItemStack> prizes = new HashMap<>();
    private static HashMap<UUID, BukkitTask> endTasks = new HashMap<>();
    private static HashMap<UUID, BukkitTask> spinTasks = new HashMap<>();

    public static void createEnchantmentsChest(Player player, boolean legendary) {
        if(prizes.size() == 0 && spinTasks.size() == 0) {
            Inventory inv = Bukkit.createInventory(player, 27, me.bright.utils.M.getMessage("enchChest"));
            startSpin(inv, player, legendary);
            ItemStack hand = player.getInventory().getItemInMainHand();
            hand.setAmount(hand.getAmount() - 1);
        }
    }

    public static void startSpin(Inventory inv, Player player, boolean legendary) {

        player.openInventory(inv);
        books = (legendary ? getMaxLevelBooks() : getAllBooks());
        booksArray = books.toArray(new ItemStack[books.size()]);

         BukkitTask spinning = new BukkitRunnable() {
            @Override
            public void run() {
                spin(inv,player,legendary);
            }

        }.runTaskTimer(PEnchantments.getPlugin(),0,5L);

        BukkitTask end = new BukkitRunnable() {
            @Override
            public void run() {
                endCase(player);
            }
        }.runTaskLater(PEnchantments.getPlugin(),100L);

        endTasks.put(player.getUniqueId(),end);
        spinTasks.put(player.getUniqueId(),spinning);
    }


    public static void spin(Inventory inv, Player player, boolean legendary) {

        ItemStack prize = (legendary ? booksArray[random.nextInt(booksArray.length - 1)] : getPrize());

        inv.setItem(0,getRandomPaneColor());
        inv.setItem(1,getRandomPaneColor());
        inv.setItem(2,getRandomPaneColor());
        inv.setItem(3,getRandomPaneColor());
        inv.setItem(4,getGoldItem());
        inv.setItem(5,getRandomPaneColor());
        inv.setItem(6,getRandomPaneColor());
        inv.setItem(7,getRandomPaneColor());
        inv.setItem(8,getRandomPaneColor());


        inv.setItem(9,booksArray[random.nextInt(booksArray.length - 1)]);
        inv.setItem(10,booksArray[random.nextInt(booksArray.length - 1)]);
        inv.setItem(11,booksArray[random.nextInt(booksArray.length - 1)]);
        inv.setItem(12,booksArray[random.nextInt(booksArray.length - 1)]);
        inv.setItem(13,prize);
        inv.setItem(14,booksArray[random.nextInt(booksArray.length - 1)]);
        inv.setItem(15,booksArray[random.nextInt(booksArray.length - 1)]);
        inv.setItem(16,booksArray[random.nextInt(booksArray.length - 1)]);
        inv.setItem(17,booksArray[random.nextInt(booksArray.length - 1)]);

        inv.setItem(18,getRandomPaneColor());
        inv.setItem(19,getRandomPaneColor());
        inv.setItem(20,getRandomPaneColor());
        inv.setItem(21,getRandomPaneColor());
        inv.setItem(22,getGoldItem());
        inv.setItem(23,getRandomPaneColor());
        inv.setItem(24,getRandomPaneColor());
        inv.setItem(25,getRandomPaneColor());
        inv.setItem(26,getRandomPaneColor());
        prizes.put(player.getUniqueId(),prize);
    }

    private static HashSet<ItemStack> getAllBooks() {

        HashSet<ItemStack> books = new HashSet<ItemStack>();
        for (PEnchantment ench : PEnchantment.values()) {

            int level = ench.getMinLevel();
            books.add(PEManager.getBook(ench,level));
            while(level <= ench.getMaxLevel()) {
                books.add(PEManager.getBook(ench,level));
                level++;
            }

        }
        return books;
    }

    private static HashSet<ItemStack> getMaxLevelBooks() {
        HashSet<ItemStack> books = new HashSet<ItemStack>();
        for (PEnchantment ench : PEnchantment.values()) {
            books.add(PEManager.getBook(ench,ench.getMaxLevel()));
        }
        return books;
    }


    public static ItemStack getRandomPaneColor() {
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)random.nextInt(16));
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        return glass;
    }

    public static ItemStack getGoldItem() {
        ItemStack gold = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta goldMeta = gold.getItemMeta();
        goldMeta.setDisplayName(" ");
        gold.setItemMeta(goldMeta);
        return gold;
    }

    public static ItemStack getPrize() {
        int chance = Chance.getRandomNumber(1,100);
        List<ItemStack> books = getBooksByChance(chance);
        return books.get(random.nextInt(books.size()));
    }

    public static List<ItemStack> getBooksByChance(int chance) {
        List<ItemStack> books = new ArrayList<>();
        int i = 0;
        for(PEnchantment ench : PEnchantment.values()) {
            if(chance <= ench.getChance()) {
                books.add(PEManager.getBook(ench,Chance.getRandomNumber(ench.getMinLevel(),ench.getMaxLevel())));
            }
            i++;
        }
        return books;
    }

    public static void endCase(Player player) {
        spinTasks.get(player.getUniqueId()).cancel();
        player.getInventory().addItem(prizes.get(player.getUniqueId()));
        player.updateInventory();
        prizes.clear();
        spinTasks.clear();
        endTasks.clear();
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        if (prizes.containsKey(player.getUniqueId())) {
            endTasks.get(player.getUniqueId()).cancel();
            endCase(player);
        }
    }
}
