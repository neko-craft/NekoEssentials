package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandName("sudo")
public final class SudoCommand extends BasicCommand {
    public SudoCommand(Main main) {
        super(main);
    }

    @Override
    public boolean callback(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || args.length < 2) return false;
        final Player p = instance.getPlayer(sender, args[0]);
        if (p == null) return true;
        sender.sendMessage(p.performCommand(StringUtils.join(
                Arrays.copyOfRange(args, 1, args.length - 1))) ? "§a执行成功!" : "§a失败成功!");
        return true;
    }
}
