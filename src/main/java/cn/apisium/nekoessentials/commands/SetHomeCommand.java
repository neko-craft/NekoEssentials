package cn.apisium.nekoessentials.commands;

import cn.apisium.nekoessentials.Main;
import cn.apisium.nekoessentials.utils.DatabaseSingleton;
import cn.apisium.nekoessentials.utils.Serializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandName("sethome")
public final class SetHomeCommand extends BasicCommand {
    public SetHomeCommand(Main main) {
        super(main);
    }

    @Override
    public boolean callback(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return false;
        final Player p = (Player) sender;
        DatabaseSingleton.INSTANCE.setPlayerData(p, "home", Serializer.serializeLocation(p.getLocation()));
        p.sendMessage("°Ïa≥…π¶…Ë÷√!");
        return true;
    }
}
