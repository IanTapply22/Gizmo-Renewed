package com.iantapply.gizmo.command.commands;

import com.iantapply.gizmo.Gizmo;
import com.iantapply.gizmo.command.CommandPermission;
import com.iantapply.gizmo.command.GizmoCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.iantapply.gizmo.data.Placeholders.gizmoPrefixNoDec;
import static com.iantapply.gizmo.data.Utilities.chatTranslate;

public class HelpCommand extends GizmoCommand {

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String syntax() {
        return "help";
    }

    @Override
    public String description() {
        return "Shows this help menu!";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public int minArgs() {
        return 0;
    }

    @Override
    public int maxArgs() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.usage(sender);
    }

    private void usage(CommandSender sender) {
        if (sender.hasPermission(CommandPermission.RELOAD.getPermission())) {
            sender.sendMessage("");
            sender.sendMessage(chatTranslate("#ee0000Usages:"));
            sender.sendMessage(chatTranslate("&f/gizmo fade <in> <stay> <out> [player] &7- Displays a fade."));
            sender.sendMessage(chatTranslate("&f/gizmo reload &7- Reloads the Gizmo configs (not recommended)."));
            sender.sendMessage(chatTranslate("&f/gizmo show <player> &7- Force displays the welcome screen."));
            sender.sendMessage("");
            sender.sendMessage(chatTranslate(gizmoPrefixNoDec() + " " + Gizmo.getInstance().getDescription().getVersion() + " &f- Made by Jeqo (&nhttps://jeqo.net&r)"));
            sender.sendMessage("");
        } else {
            sender.sendMessage("");
            sender.sendMessage(chatTranslate(gizmoPrefixNoDec() + " " + Gizmo.getInstance().getDescription().getVersion() + " &f- Made by Jeqo (&nhttps://jeqo.net&r)"));
            sender.sendMessage("");
        }
    }
}
