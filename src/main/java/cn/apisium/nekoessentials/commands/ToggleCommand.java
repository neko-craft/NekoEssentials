package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.Main;
import cn.apisium.nekoessentials.utils.Serializer;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandName("toggle")
public final class ToggleCommand extends BasicCommand {
    public ToggleCommand(Main main) { super(main); }

    @Override
    public boolean callback(CommandSender sender, String[] args) {
       if (!(sender instanceof Player)) return false;
       final Player p = (Player) sender;
       final byte[] key = (p.getUniqueId().toString() + ".toggleLocation").getBytes();
       switch (p.getGameMode()) {
           case SURVIVAL:
               instance.db.put(key, Serializer.serializeLocation(p.getLocation()));
               p.setGameMode(GameMode.SPECTATOR);
               break;
           case SPECTATOR:
               final byte[] bytes = instance.db.get(key);
               if (bytes != null) {
                   instance.db.delete(key);
                   p.teleport(Serializer.deserializeLocation(bytes));
               }
               p.setGameMode(GameMode.SURVIVAL);
               break;
       }
       p.sendActionBar("§a模式切换成功!");
       return true;
    }
}
