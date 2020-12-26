package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class TeleportCommand extends TargetCommand {
    public TeleportCommand(Main main) { super(main); }

    public abstract void doAction(CommandSender sender, Player p, boolean now);
}
