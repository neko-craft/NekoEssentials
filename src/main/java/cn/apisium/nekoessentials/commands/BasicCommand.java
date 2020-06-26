package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class BasicCommand implements CommandExecutor, TabCompleter {
    public Main instance;

    public BasicCommand(Main main) {
        instance = main;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return callback(sender, args);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tabComplete(sender, args);
    }

    public abstract boolean callback(CommandSender sender, String[] args);

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
