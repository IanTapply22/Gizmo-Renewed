package com.iantapply.gizmo.listener;

import com.iantapply.gizmo.GizmoRenewed;
import com.iantapply.gizmo.data.Placeholders;
import com.iantapply.gizmo.data.ChatTranslate;
import com.iantapply.gizmo.logger.Logger;
import com.iantapply.gizmo.logger.LoggingLevel;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static com.iantapply.gizmo.data.Placeholders.screenTitle;

public class ScreenAdvanceListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if (e.getView().getTitle().equals(screenTitle())) {

            if (Objects.equals(GizmoRenewed.getInstance().getConfig().getString("enable-fade"), "true")) {
                p.sendTitle(ChatTranslate.translate(null, GizmoRenewed.getInstance().getConfigConfigurationCore().getString("background-color") + GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.background")), "", 0, 5, GizmoRenewed.getInstance().getConfigConfigurationCore().getInteger("fade-time"));
            } else {
                p.sendTitle("", "", 0, 0, 0);
            }

            try {
                if (Objects.equals(GizmoRenewed.getInstance().getConfig().getString("sound-on-advance.enable"), "true")) {
                    p.playSound(p.getLocation(), Sound.valueOf(GizmoRenewed.getInstance().getConfig().getString("sound-on-advance.sound")), Float.parseFloat(Objects.requireNonNull(GizmoRenewed.getInstance().getConfigConfigurationCore().getString("sound-on-advance.volume"))), Float.parseFloat(Objects.requireNonNull(GizmoRenewed.getInstance().getConfigConfigurationCore().getString("sound-on-advance.pitch"))));
                }
            } catch (NullPointerException ex) {
                Logger.log(LoggingLevel.WARNING, "sound-on-advance is not configured correctly.");
            }

            ScreenDisplayHandler.playersScreenActive.remove(p.getUniqueId());
            p.removePotionEffect(PotionEffectType.BLINDNESS);

            for (String command : GizmoRenewed.getInstance().getConfig().getStringList("commands-on-advance")) {
                if (command.contains("[console]")) {
                    command = command.replace("[console] ", "");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
                } else if (command.contains("[message]")) {
                    command = command.replace("[message] ", "");
                    p.sendMessage(ChatTranslate.translate(null, command.replace("%player%", p.getName())));
                } else if (command.contains("[player]")) {
                    command = command.replace("[player] ", "");
                    p.performCommand(command);
                } else {
                    p.sendMessage(Placeholders.gizmoPrefix() + "An error occurred. Please review the console for more information.");
                    Logger.log(LoggingLevel.ERROR, "Commands-on-advance (config.yml) has a command with an invalid format.");
                }
            }

            if (!p.hasPlayedBefore()) {
                if (GizmoRenewed.getInstance().getScreensConfigurationCore().getBoolean("first-join-welcome-screen")) {
                    welcomeMessageFirstJoin(p);
                }
            } else {
                welcomeMessage(p);
            }
        }
    }

    private void welcomeMessage(Player p) {
        String welcomeMessage = GizmoRenewed.getInstance().getMessagesConfigurationCore().getString("welcome-message");

        if (welcomeMessage == null) return;

        welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
        p.sendMessage(ChatTranslate.translate(null, welcomeMessage));
    }

    private void welcomeMessageFirstJoin(Player p) {
        String welcomeMessage = (GizmoRenewed.getInstance().getMessagesConfigurationCore().getString("first-join-welcome-message"));

        if (welcomeMessage == null) return;

        welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
        p.sendMessage(ChatTranslate.translate(null, welcomeMessage));
    }
}