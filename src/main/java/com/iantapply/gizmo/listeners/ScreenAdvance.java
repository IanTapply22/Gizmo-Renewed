package com.iantapply.gizmo.listeners;

import com.iantapply.gizmo.Gizmo;
import com.iantapply.gizmo.data.Placeholders;
import com.iantapply.gizmo.data.Utilities;
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

public class ScreenAdvance implements Listener {
    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);


    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if (e.getView().getTitle().equals(screenTitle())) {

            if (Objects.equals(plugin.getConfig().getString("enable-fade"), "true")) {
                p.sendTitle(Utilities.chatTranslate(Gizmo.getInstance().getConfigConfigurationCore().getString("background-color") + Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.background")), "", 0, 5, Gizmo.getInstance().getConfigConfigurationCore().getInteger("fade-time"));
            } else {
                p.sendTitle("", "", 0, 0, 0);
            }


            try {
                if (Objects.equals(plugin.getConfig().getString("sound-on-advance.enable"), "true")) {
                    p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound-on-advance.sound")), Float.parseFloat(Objects.requireNonNull(Gizmo.getInstance().getConfigConfigurationCore().getString("sound-on-advance.volume"))), Float.parseFloat(Objects.requireNonNull(Gizmo.getInstance().getConfigConfigurationCore().getString("sound-on-advance.pitch"))));
                }
            } catch (NullPointerException ex) {
                Logger.log(LoggingLevel.WARNING, "sound-on-advance is not configured correctly.");
            }


            PlayerScreening.playersScreenActive.remove(p.getUniqueId());
            p.removePotionEffect(PotionEffectType.BLINDNESS);


            for (String command : plugin.getConfig().getStringList("commands-on-advance")) {
                if (command.contains("[console]")) {
                    command = command.replace("[console] ", "");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
                } else if (command.contains("[message]")) {
                    command = command.replace("[message] ", "");
                    p.sendMessage(Utilities.chatTranslate(command.replace("%player%", p.getName())));
                } else if (command.contains("[player]")) {
                    command = command.replace("[player] ", "");
                    p.performCommand(command);
                } else {
                    p.sendMessage(Placeholders.gizmoPrefix() + "An error occurred. Please review the console for more information.");
                    Logger.log(LoggingLevel.ERROR, "Commands-on-advance (config.yml) has a command with an invalid format.");
                }
            }


            if (!p.hasPlayedBefore()) {
                if (Gizmo.getInstance().getScreensConfigurationCore().getBoolean("first-join-welcome-screen")) {
                    welcomeMessageFirstJoin(p);
                }
            } else {
                welcomeMessage(p);
            }
        }
    }



    private void welcomeMessage(Player p) {
        String welcomeMessage = Gizmo.getInstance().getMessagesConfigurationCore().getString("welcome-message");

        if (welcomeMessage == null) {
            return;
        }

        welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
        p.sendMessage(Utilities.chatTranslate(welcomeMessage));
    }



    private void welcomeMessageFirstJoin(Player p) {
        String welcomeMessage = (Gizmo.getInstance().getMessagesConfigurationCore().getString("first-join-welcome-message"));
        assert welcomeMessage != null;
        if (welcomeMessage.equals("[]")) {
            return;
        } else {
            welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
            p.sendMessage(Utilities.chatTranslate(welcomeMessage));
        }
    }
}