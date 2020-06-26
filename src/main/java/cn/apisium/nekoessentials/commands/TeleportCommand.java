package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.*;
import cn.apisium.nekoessentials.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class TeleportCommand extends BasicCommand {
    public TeleportCommand(Main main) {
        super(main);
    }

    @Override
    public boolean callback(CommandSender sender, String[] args) {
        switch (args.length) {
            case 0:
                if (!(sender instanceof Player)) return false;
                doTeleport(sender, (Player) sender, false);
                return true;
            case 1:
                if (!Utils.canTeleportOthers(sender)) return false;
                final Player p = instance.getServer().getPlayerExact(args[0]);
                if (p == null) sender.sendMessage(Constants.NO_SUCH_PLAYER);
                else doTeleport(sender, p, true);
                return true;
        }
        return true;
    }

    public abstract void doTeleport(CommandSender sender, Player p, boolean now);
}
