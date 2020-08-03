package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.*;
import cn.apisium.nekoessentials.utils.DatabaseSingleton;
import cn.apisium.nekoessentials.utils.Serializer;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandName("back")
public final class BackCommand extends TeleportCommand {
    public BackCommand(Main main) {
        super(main);
    }

    @Override
    public void doAction(CommandSender sender, Player p, boolean now) {
        final byte[] bytes = DatabaseSingleton.INSTANCE.getPlayerData(p, "lastLocation");
        if (bytes == null) sender.sendMessage("§c找不到上一个位置!");
        else {
            final Location location = Serializer.deserializeLocation(bytes);
            instance.delayTeleport(p, location, now);
        }
    }
}
