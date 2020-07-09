package cn.apisium.nekoessentials;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public final class Constants {
    public static final String COMMAND_DESCRIPTION = "A NekoEssentials provided command.";
    public static final String WRONG_USAGE = "§c错误的命令用法!";
    public static final String NO_PERMISSION = "§c你没有权限来执行这个指令!";
    public static final String NO_SUCH_PLAYER = "§c没有这样的一个玩家!";

    public static final String MESSAGE_HEADER = "§b§m                    §r §e[传送系统] §b§m                    ";
    public static final String MESSAGE_FOOTER = "§b§m                                                       ";
    public static final BaseComponent[] CANCEL_HUB = new BaseComponent[3];
    public static final BaseComponent[] REQUEST_HUB = new BaseComponent[4];

    static {
        CANCEL_HUB[0] = new TextComponent("  §d正在传送中... §7传送中禁止移动, 如需取消请点击下面的文字:\n");
        CANCEL_HUB[1] = new TextComponent("                  ");
        TextComponent t = new TextComponent("[取消传送]");
        t.setColor(ChatColor.RED);
        t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpcancel"));
        CANCEL_HUB[2] = t;

        REQUEST_HUB[0] = new TextComponent("            ");
        t = new TextComponent("[拒绝传送]");
        t.setColor(ChatColor.RED);
        t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
        REQUEST_HUB[1] = t;
        REQUEST_HUB[2] = new TextComponent("    ");
        t = new TextComponent("[确认传送]");
        t.setColor(ChatColor.GREEN);
        t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        REQUEST_HUB[3] = t;
    }
}
