package me.bright.util;

import me.bright.PEnchantments;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class M {
    private static PEnchantments plugin = PEnchantments.getPlugin();




    public static void msg(CommandSender sender, String message) {
        sender.sendMessage(M.colored(message));
    }

    public static String colored(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    public static List<String> asList(String... strings) {
        List<String> lore = new ArrayList<>();
        for(String s : strings) {
            lore.add(M.colored(s));
        }
        return lore;
    }



}
