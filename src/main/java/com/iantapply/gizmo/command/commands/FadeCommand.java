package com.iantapply.gizmo.command.commands;

import com.iantapply.gizmo.GizmoRenewed;
import com.iantapply.gizmo.command.CommandPermission;
import com.iantapply.gizmo.command.GizmoCommand;
import com.iantapply.gizmo.data.ChatTranslate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.iantapply.gizmo.data.Placeholders.gizmoPrefix;

public class FadeCommand extends GizmoCommand {

    @Override
    public String name() {
        return "fade";
    }

    @Override
    public String syntax() {
        return "fade <in> <stay> <out> [player]";
    }

    @Override
    public String description() {
        return "Displays a fade.";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public int minArgs() {
        return 3;
    }

    @Override
    public int maxArgs() {
        return 4;
    }

    @Override
    public ArrayList<CommandPermission> requiredCommandPermissions() {
        ArrayList<CommandPermission> permissions = new ArrayList<>();

        permissions.add(CommandPermission.FADE);

        return permissions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player senderPlayer = (Player) sender;

        if (args.length == 4) {
            Player target = Bukkit.getPlayer(args[3]);
            if (target == null) {
                sender.sendMessage(ChatTranslate.translate(null, gizmoPrefix() + "&7That player is not online!"));
                return;
            }
            checkRegex(sender, args, target);
        } else if (args.length == 3) {
            checkRegex(sender, args, senderPlayer);
        }
    }

    private void checkRegex(CommandSender sender, String[] args, Player target) {
        if (args[0].matches("[0-9]+") && args[1].matches("[0-9]+") && args[2].matches("[0-9]+")) {
            target.sendTitle(GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.background"), "", Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } else {
            sender.sendMessage(ChatTranslate.translate(null, gizmoPrefix() + "&7Only numbers can be used for time values!"));
        }
    }
}
