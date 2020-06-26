package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.Main;
import cn.apisium.nekoessentials.utils.Serializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.HashSet;

@CommandName("mute")
public final class MuteCommand extends BasicCommand implements Listener {
    private final HashSet<String> set;
    private final static String MUTED_LIST = "mutedList";
    @SuppressWarnings("unchecked")
    public MuteCommand(Main main) {
        super(main);
        final byte[] bytes = main.db.get(MUTED_LIST);
        HashSet<String> s = null;
        try {
            if (bytes != null) s = (HashSet<String>) Serializer.deserializeObject(bytes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        set = s == null ? new HashSet<>() : s;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if (set.contains(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
            e.getPlayer().sendActionBar("§c发送失败! 您已被禁言!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean callback(CommandSender sender, String[] args) {
       if (args.length != 1) return false;
       final OfflinePlayer o = instance.getServer().getOfflinePlayer(args[0]);
       if (!o.hasPlayedBefore()) {
           sender.sendMessage("§c该玩家还从未在服务器游玩过!");
           return true;
       }
       final String id = o.getUniqueId().toString();
       final Player p = o.getPlayer();
       if (set.contains(id)) {
           set.remove(id);
           if (p != null) p.sendMessage("§a您已被解除禁言!");
       } else {
           set.add(id);
           if (p != null) p.sendMessage("§c您已被禁言!");
       }
       try {
           instance.db.set(MUTED_LIST, Serializer.serializeObject(set));
       } catch (IOException e) {
           e.printStackTrace();
       }
       return true;
    }
}
