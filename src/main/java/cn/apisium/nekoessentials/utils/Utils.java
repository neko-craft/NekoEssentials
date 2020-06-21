package cn.apisium.nekoessentials.utils;

import cn.apisium.nekoessentials.Constants;
import cn.apisium.nekoessentials.Main;
import cn.apisium.nekoessentials.commands.BasicCommand;
import cn.apisium.nekoessentials.commands.CommandName;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.PluginCommand;

public final class Utils {
    private Utils() {}
    @SafeVarargs
    public static void loadCommands (Main main, Class<? extends BasicCommand> ...commands) throws Exception {
        for (Class<? extends BasicCommand> it : commands) {
            final CommandName name = it.getAnnotation(CommandName.class);
            assert name != null;
            final BasicCommand exec = it.getConstructor(Main.class).newInstance(main);
            final PluginCommand cmd = main.getServer().getPluginCommand(name.value());
            assert cmd != null;
            cmd.setUsage(Constants.WRONG_USAGE);
            cmd.setPermissionMessage(Constants.NO_PERMISSION);
            cmd.setExecutor(exec);
            cmd.setTabCompleter(exec);
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isSafeLocation(Location location) {
        final Block feet = location.getBlock();
        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
            return true;
        }
        final Block head = feet.getRelative(BlockFace.UP);
        if (!head.getType().isTransparent()) {
            return true;
        }
        final Block ground = feet.getRelative(BlockFace.DOWN);
        return !ground.getType().isSolid();
    }
}
