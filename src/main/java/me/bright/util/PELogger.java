package me.bright.util;

import me.bright.PEnchantments;

public class PELogger {

    private static PEnchantments plugin = PEnchantments.getPlugin();




    public static void error(String message) {
        plugin.getServer().getConsoleSender().sendMessage("§c" + message);
    }

    public static void success(String message) {
        plugin.getServer().getConsoleSender().sendMessage("§a" + message);
    }
}
