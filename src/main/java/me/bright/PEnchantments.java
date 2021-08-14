package me.bright;

import me.bright.commands.GetBookCommand;
import me.bright.enchantments.*;
import me.bright.armor.ArmorListener;
import me.bright.listeners.PEListener;
import me.bright.util.EnchChest;
import me.bright.util.PEManager;
import me.bright.utils.PrisonLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class PEnchantments extends JavaPlugin {
    private static PEnchantments plugin;
    private CommandMap commandMap;
    private Field bukkitCommandMap;

    @Override
    public void onEnable() {
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new ArmorListener(),this);
        this.getServer().getPluginManager().registerEvents(new PEListener(),this);
        this.getServer().getPluginManager().registerEvents(new Armor(),this);
        this.getServer().getPluginManager().registerEvents(new DamageList(),this);
        this.getServer().getPluginManager().registerEvents(new Tools(),this);
        this.getServer().getPluginManager().registerEvents(new EnchChest(),this);
        this.getServer().getPluginManager().registerEvents(new Bow(),this);
        this.getServer().getPluginManager().registerEvents(new CharmerListener(),this);
        PEManager.load();
        loadCommandMap();
        registerCommands();
    }

    @Override
    public void onDisable() {

    }

    public static PEnchantments getPlugin() {
        return plugin;
    }


    public void registerCommands() {
        commandMap.register("getbook",new GetBookCommand());
    }

    private void loadCommandMap() {
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            PrisonLogger.error("Не удается создать commandMap\n" + e.getMessage());
        }
    }
}
