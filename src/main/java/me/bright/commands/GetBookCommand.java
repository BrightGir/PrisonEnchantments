package me.bright.commands;

import me.bright.enums.PEnchantment;
import me.bright.util.M;
import me.bright.util.PEManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetBookCommand extends Command {

    public GetBookCommand() {
        super("getbook");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }

        if(args.length != 2) {
            M.msg(sender,"&cИспользование команды - /getbook <имя> <количество>");
            return true;
        }

        try {
            String enchName = args[0].toUpperCase();
            PEnchantment ench = PEnchantment.valueOf(enchName);
            int level = Integer.parseInt(args[1]);

            if(level > ench.getMaxLevel() || level < ench.getMinLevel()) {
                M.msg(sender,"&cМаксимальный уровень - " + ench.getMaxLevel() + ", минимальный - " + ench.getMinLevel());
                return true;
            }

            Player player = (Player) sender;
            player.getInventory().addItem(PEManager.getBook(ench,level));
            player.updateInventory();
            M.msg(sender,"&aВы успешно получили книгу");
        } catch (Exception e) {
            M.msg(sender,"&cЧто-то пошло не так - " + e.getMessage());
        }
        return true;
    }
}
