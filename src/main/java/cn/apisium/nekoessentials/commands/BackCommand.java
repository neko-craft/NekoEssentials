package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.*;
import cn.apisium.nekoessentials.utils.Serializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandName("back")
public final class BackCommand extends TeleportCommand {
    public BackCommand(Main main) {
        super(main);
    }

    public void doTeleport(CommandSender sender, Player p, boolean now) {
        final byte[] bytes = instance.db.get((p.getUniqueId().toString() + ".lastLocation").getBytes());
        if (bytes == null) sender.sendMessage("§c找不到上一个位置!");
        else instance.delayTeleport(p, Serializer.deserializeLocation(bytes), now);
    }
}
