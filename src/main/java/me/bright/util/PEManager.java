package me.bright.util;

import me.bright.PEnchantments;
import me.bright.enums.PEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.*;

public class PEManager {

    private static Random rnd = new Random();
    private static HashMap<String, Integer> romeValue = new HashMap<>();

    public static void load() {
        romeValue.put("I",1);
        romeValue.put("II",2);
        romeValue.put("III",3);
        romeValue.put("IV",4);
        romeValue.put("V",5);
    }

    public static void addEnchantment(ItemStack item, PEnchantment ench, int level) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;
        if(meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }

        String enchName = ench.getDisplayName();

        if(!hasEnchantments(item)) {
            lore.add(" ");
            lore.add(enchName + " " + getRomeLevelByInt(level));
        } else {
            lore.add(enchName + " " + getRomeLevelByInt(level));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void removeEnchantments(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> resultLore = meta.getLore();
        resultLore.remove(2);
        resultLore.remove(2);
        if(resultLore.size() >= 3) resultLore.remove(2);
        meta.setLore(resultLore);
        item.setItemMeta(meta);
    }



    public static void removeEnchantment(ItemStack item, PEnchantment ench) {
        String enchString = "";
        ItemMeta meta = item.getItemMeta();
        List<String> resultLore = meta.getLore();
        for(String lore : resultLore) {
            if(lore.contains(ench.getDisplayName())) {
                enchString = lore;
                continue;
            }
        }
        resultLore.remove(enchString);
        meta.setLore(resultLore);
        item.setItemMeta(meta);
    }

    public static void removeEnchantmentByString(Player player, ItemStack item, String enchString) {
        // так сделано потому что сюда может попасть предмет не из инвентаря игрока.. может когда-нибудь исправлю эту хрень
        try {
            int slot = getSlot(player.getInventory(), item);
            ItemStack current = player.getInventory().getItem(slot);
            ItemMeta meta = current.getItemMeta();
            List<String> resultLore = meta.getLore();
            String.valueOf(resultLore.remove(enchString));
            meta.setLore(resultLore);
            current.setItemMeta(meta);
            player.getInventory().setItem(slot,current);
        } catch (Exception e) {
            M.msg(player,"&cУпс! Что-то пошло не так, обратитесь к администратору, сообща этот код ошибки: " + Exceptions.REMOVEENCHANTMENT.getNumber());
        }
    }

    private static int getSlot(PlayerInventory inv, ItemStack item) {
        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) != null && inv.getItem(i).isSimilar(item)) {
                return i;
            }
        }
        return -100;
    }

    public static void removeEnchantment(ItemStack item, PEnchantment ench, String level) {
        ItemMeta meta = item.getItemMeta();
        List<String> resultLore = meta.getLore();
        String enchName = ench.getDisplayName() + " " + level;
        resultLore.remove(enchName);
        meta.setLore(resultLore);
        item.setItemMeta(meta);
      //  int index = 0;
  //      ItemMeta meta = item.getItemMeta();
  //      List<String> resultLore = meta.getLore();
  //      String enchName = ench.getDisplayName() + " " + level;
  //      for(String s : resultLore) {
  //          if(s.equals(enchName)) {
  //              PELogger.error(s + " " + enchName);
  //              PELogger.error("equals index = " + index);
  //              continue;
  //          }
  //          index++;
  //      }
  //      PELogger.error(resultLore.remove(index));
  //      meta.setLore(resultLore);
  //      item.setItemMeta(meta);
    }



    public static List<Integer> getLevelsOfSpecifyEnchantment(ItemStack item, PEnchantment ench) {
        List<Integer> list = new ArrayList<>();
        for(String s : item.getItemMeta().getLore()) {
            String displayName = ench.getDisplayName();
            if(s.contains(displayName)) {
                String levelString = s.replace(displayName,"");
                levelString = levelString.trim();
                int lvl = getIntByRome(levelString);
                list.add(lvl);
            }
        }
        return list;
    }


    public static String getRomeLevelByInt(int level) {
        String romeLevel = "0";
        for(Map.Entry<String,Integer> entry : romeValue.entrySet()) {
            if(entry.getValue().equals(level)) {
                romeLevel = entry.getKey();
                break;
            }
        }
        return romeLevel;
    }

    public static HashSet<PEnchantment> getEnchantments(ItemStack item) {
        HashSet<PEnchantment> list = new HashSet<>();
        for(PEnchantment ench : PEnchantment.values()) {
            if(hasEnchantment(item,ench)) {
                list.add(ench);
            }
        }
        return list;
    }

    public static HashMap<PEnchantment, Integer> getEnchantmentsMap(ItemStack item) {
        HashMap<PEnchantment,Integer> enchantments = new HashMap<>();
        for(PEnchantment ench : PEnchantment.values()) {
            if(hasEnchantment(item,ench)) {
                enchantments.put(ench,ench.getLevel(item));
            }
        }
        return enchantments;
    }

    public static void addEnchantments(ItemStack item, HashMap<PEnchantment,Integer> enchantments) {
        for(Map.Entry entry : enchantments.entrySet()) {
            addEnchantment(item,(PEnchantment)entry.getKey(),(int)entry.getValue());
        }
    }

    public static boolean hasEnchantment(ItemStack item, PEnchantment ench) {
        try {
            for (String string : item.getItemMeta().getLore()) {
                if (string.contains(ench.getDisplayName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static int getNumberOfSpecifyEnchantment(ItemStack item, PEnchantment ench) {
        int i = 0;
        try {
            for (String string : item.getItemMeta().getLore()) {
                if (string.contains(ench.getDisplayName())) {
                    i++;
                }
            }
        } catch (Exception e) {

        }
        return i;
    }


    public static int getSumOfLevel(ItemStack item, PEnchantment ench) {
        int i = 0;
        for (String string : item.getItemMeta().getLore()) {
            if (string.contains(ench.getDisplayName())) {
                i = i + ench.getLevel(item);
            }
        }
        return i;
    }

    public static boolean hasEnchantments(ItemStack item) {
        for (PEnchantment enchantment : PEnchantment.values()) {
            if (hasEnchantment(item, enchantment))
                return true;
        }
        return false;
    }

    public static int getEnchantmentsSize(ItemStack item) {
        int i = 0;
        for (PEnchantment enchantment : PEnchantment.values()) {
            if (hasEnchantment(item, enchantment)) {
                i = i + getNumberOfSpecifyEnchantment(item,enchantment);
            }
        }
        return i;
    }

    public static void setChance(ItemStack book, int newChance, int oldChance) {
            ItemMeta meta = book.getItemMeta();
            List<String> resultLore = meta.getLore();
            resultLore.set(0,resultLore.get(0).replace(String.valueOf(oldChance),String.valueOf(newChance)));
            meta.setLore(resultLore);
            book.setItemMeta(meta);
    }


    public static ItemStack getBook(PEnchantment enchantment, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        String lvlRome = " ";
        bookMeta.setDisplayName(enchantment.getDisplayName() + " " + getRomeLevelByInt(level));
        List<String> bookLore = new ArrayList<>();
        bookLore.add(M.colored("&6" + Messages.CHANCE + "&a" + Chance.getRandomNumber(9,25) + "%"));
        bookLore.add(" ");
        bookLore.addAll(enchantment.getLore());
        bookLore.add(" ");
        bookLore.add(M.colored("&6Вид: ") + enchantment.getType().getDisplayName());
        bookMeta.setLore(bookLore);
        book.setItemMeta(bookMeta);
        return book;
    }



    public static PEnchantment getEnchantmentInBook(ItemStack item) {
        return PEnchantment.getByName(item.getItemMeta().getDisplayName());
    }


    public static int getIntByRome(Object value) {
        return romeValue.get(value);
    }

    public static int getChance(List<String> lore) {
        int chance = 0;
        try {

            String chanceString = lore.get(0);
            chanceString = chanceString.replace(Messages.CHANCE,"");
            chanceString = ChatColor.stripColor(chanceString);
            chance = Integer.parseInt(chanceString.replace("%",""));

        } catch (Exception e) {

            for(String string : lore) {
                if(string.contains("%")) {
                    string = string.replace(Messages.CHANCE,"");
                    string = string.trim();
                    string = string.replace("%","");
                    string = ChatColor.stripColor(string);
                    chance = Integer.parseInt(string);
                }
            }

        }
        return chance;
    }


















}
