package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.Main;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandName("spawn")
public final class SpawnCommand extends TargetCommand {
    @SuppressWarnings("ConstantConditions")
    private final Location spawn = instance.getServer().getWorld("world").getSpawnLocation();

    public SpawnCommand(Main main) {
        super(main);
    }

    @Override
    public void doAction(CommandSender sender, Player p, boolean now) {
        instance.delayTeleport(p, spawn, now);
    }
}
