package cn.apisium.nekoessentials.utils;

import cn.apisium.nekoessentials.Constants;
import cn.apisium.nekoessentials.Main;
import cn.apisium.nekoessentials.commands.BasicCommand;
import cn.apisium.nekoessentials.commands.CommandName;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public final class Utils {
    private Utils() {
    }

    @SafeVarargs
    public static void loadCommands(Main main, Class<? extends BasicCommand>... commands) throws Exception {
        for (Class<? extends BasicCommand> it : commands) {
            final CommandName name = it.getAnnotation(CommandName.class);
            assert name != null;
            final BasicCommand exec = it.getConstructor(Main.class).newInstance(main);
            final PluginCommand cmd = main.getServer().getPluginCommand(name.value());
            assert cmd != null;
            cmd.setUsage(Constants.WRONG_USAGE);
            cmd.setPermissionMessage(Constants.NO_PERMISSION);
            cmd.setDescription(Constants.COMMAND_DESCRIPTION);
            cmd.setExecutor(exec);
            cmd.setTabCompleter(exec);
        }
    }

    public static Location findSafeLocation(Location loc) {
        int y = loc.getBlockY();
        while (y > 0) {
            final Material b = loc.getBlock().getType();
            if (b == Material.WATER || b.isSolid()) {
                loc.setY(y + 1);
                return loc;
            }
            if (b == Material.LAVA) break;
            loc.setY(--y);
        }
        return null;
    }

    public static boolean canTeleportOthers(CommandSender who) {
        return who.hasPermission("nekoess.others");
    }

    public static void teleportPlayer(Player player, Entity entity) {
        teleportPlayer(player, entity.getLocation());
    }

    public static void teleportPlayer(Player player, Location location) {
        final Location lastLocation = player.getLocation();
        player.teleport(location);
        recordPlayerLocation(player, lastLocation);
    }

    public static void recordPlayerLocation(Player player) {
        recordPlayerLocation(player, player.getLocation());
    }

    public static void recordPlayerLocation(Player player, Location loc) {
        DatabaseSingleton.INSTANCE.setPlayerData(player, "lastLocation", Serializer.serializeLocation(loc));
    }
}
