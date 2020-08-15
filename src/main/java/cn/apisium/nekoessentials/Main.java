package cn.apisium.nekoessentials;

import cn.apisium.nekoessentials.commands.*;
import cn.apisium.nekoessentials.utils.*;
import com.destroystokyo.paper.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.scheduler.BukkitTask;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

@Plugin(name = "NekoEssentials", version = "1.0")
@Description("An essential plugin used in NekoCraft.")
@Author("Shirasawa")
@Website("https://apisium.cn")
@Command(name = "afk", permission = "nekoess.afk")
@Command(name = "back", permission = "nekoess.back")
@Command(name = "db", permission = "nekoess.db")
@Command(name = "disrobe", permission = "nekoess.disrobe")
@Command(name = "tpcancel")
@Command(name = "home", permission = "nekoess.home")
@Command(name = "mute", permission = "nekoess.mute")
@Command(name = "othershome", permission = "nekoess.others")
@Command(name = "sethome", permission = "nekoess.home")
@Command(name = "spawn", permission = "nekoess.spawn")
@Command(name = "status", permission = "nekoess.status")
@Command(name = "sudo", permission = "nekoess.sudo")
@Command(name = "toggle", permission = "nekoess.toggle")
@Command(name = "tpaall", permission = "nekoess.tpaall")
@Command(name = "tpaccept")
@Command(name = "tpa", permission = "nekoess.tpa")
@Command(name = "tpdeny")
@Command(name = "tphere", permission = "nekoess.tphere")
@Command(name = "freeze", permission = "nekoess.freeze")
@Permission(name = "nekoess.afk", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.spawn", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.home", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.tpa", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.tphere", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.back", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.toggle", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.tpaall", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.status", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.disrobe", defaultValue = PermissionDefault.TRUE)
@Permission(name = "nekoess.others")
@Permission(name = "nekoess.sudo.avoid")
@Permission(name = "nekoess.immediate")
@Permission(name = "nekoess.sudo")
@Permission(name = "nekoess.mute")
@Permission(name = "nekoess.db")
@Permission(name = "nekoess.freeze")
@ApiVersion(ApiVersion.Target.v1_13)
public final class Main extends JavaPlugin {
    public final WeakHashMap<Player, Pair<Integer, Location>> countdowns = new WeakHashMap<>();
    public final WeakHashMap<Player, Pair<Long, Runnable>> playerTasks = new WeakHashMap<>();
    public final WeakHashMap<Player, Pair<Location, Long>> afkPlayers = new WeakHashMap<>();
    public static Main INSTANCE;
    public final HashSet<String> mutedPlayers = new HashSet<>();
    private final WeakHashMap<Player, Long> delays = new WeakHashMap<>();
    private BukkitTask countdownTask;

    {
        INSTANCE = this;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        try {
            if (!getDataFolder().exists()) getDataFolder().mkdir();
            DatabaseSingleton.init(Iq80DBFactory.factory.open(new File(getDataFolder(), "database"),
                    new Options().createIfMissing(true)));
        } catch (IOException e) {
            e.printStackTrace();
            setEnabled(false);
            return;
        }
        getServer().getPluginManager().registerEvents(new Events(this), this);
        try {
            Utils.loadCommands(
                    this,
                    AfkCommand.class,
                    BackCommand.class,
                    CancelCommand.class,
                    DbCommand.class,
                    DisrobeCommand.class,
                    FreezeCommand.class,
                    HomeCommand.class,
                    MuteCommand.class,
                    OthersHomeCommand.class,
                    SetHomeCommand.class,
                    SpawnCommand.class,
                    StatusCommand.class,
                    SudoCommand.class,
                    ToggleCommand.class,
                    TpaAllCommand.class,
                    TpAcceptCommand.class,
                    TpaCommand.class,
                    TpDenyCommand.class,
                    TpHereCommand.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            setEnabled(false);
            return;
        }
        countdownTask = getServer().getScheduler().runTaskTimer(this, () -> {
            final Iterator<Map.Entry<Player, Pair<Integer, Location>>> iterator = countdowns.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Player, Pair<Integer, Location>> it = iterator.next();
                final Player p = it.getKey();
                final Pair<Integer, Location> pair = it.getValue();
                if (--pair.left < 1) {
                    iterator.remove();
                    final Location dest = pair.right;
                    if (dest == null) continue;
                    Utils.teleportPlayer(p, dest);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                    p.sendActionBar("§a传送成功!");
                } else p.sendActionBar("§e将在 §b" + pair.left + "秒 §e后进行传送!");
            }
        }, 20, 20);
    }

    @Override
    public void onDisable() {
        if (countdownTask != null) countdownTask.cancel();
        countdowns.clear();
        playerTasks.clear();
        try {
            DatabaseSingleton.closeDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countdownTask = null;
    }

    public void delayTeleport(final Player player, final Location loc) {
        delayTeleport(player, loc, false);
    }

    public void delayTeleport(final Player player, Location loc, final boolean now) {
        boolean isSafe = true;
        if (player.getGameMode() == GameMode.SURVIVAL) {
            final Location temp = Utils.findSafeLocation(loc);
            if (temp == null) isSafe = false;
            else loc = temp;
        }
        if (now || (!shouldPlayerBeDelayed(player) && isSafe)) {
            countdowns.put(player, new Pair<>(1, loc));
        } else {
            player.sendMessage(Constants.MESSAGE_HEADER);
            if (!isSafe) player.sendTitle(new Title("§c危!", "§e检测到目标位置可能不安全!"));
            player.sendMessage(Constants.CANCEL_HUB);
            player.sendMessage(Constants.MESSAGE_FOOTER);
            countdowns.put(player, new Pair<>(10, loc));
        }
        if (!now) delays.put(player, System.currentTimeMillis() + 2 * 1000 * 60);
    }

    public void requestTeleport(Player player, String message, Runnable fn) {
        playerTasks.put(player, new Pair<>(System.currentTimeMillis() + 2 * 1000 * 60, fn));
        player.sendMessage(Constants.MESSAGE_HEADER);
        player.sendMessage(message);
        player.sendMessage(Constants.REQUEST_HUB);
        player.sendMessage(Constants.MESSAGE_FOOTER);
    }

    public boolean cancelTeleport(final Player player) {
        return countdowns.remove(player) != null;
    }

    public boolean shouldPlayerBeDelayed(Player player) {
        if (player.hasPermission("nekoess.immediate")) return false;
        final Long time = delays.get(player);
        return time != null && time > System.currentTimeMillis();
    }

    public Player getPlayer(CommandSender sender, String name) {
        final Player p = getServer().getPlayerExact(name);
        if (p == null) {
            sender.sendMessage(Constants.NO_SUCH_PLAYER);
            return null;
        }
        if (p == sender) {
            sender.sendMessage("§c你不能传送你自己!");
            return null;
        }
        return p;
    }

    @SuppressWarnings("unused")
    public boolean isAfking(final Player player) {
        final Pair<Location, Long> pair = afkPlayers.get(player);
        return pair != null && pair.right < System.currentTimeMillis();
    }
}
