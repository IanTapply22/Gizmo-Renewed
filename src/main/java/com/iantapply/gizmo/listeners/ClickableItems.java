package com.iantapply.gizmo.listeners;

import com.iantapply.gizmo.Gizmo;
import com.iantapply.gizmo.data.Placeholders;
import com.iantapply.gizmo.data.Utilities;
import com.iantapply.gizmo.logger.Logger;
import com.iantapply.gizmo.logger.LoggingLevel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static com.iantapply.gizmo.data.Placeholders.screenTitle;

public class ClickableItems implements Listener {

    static Gizmo plugin = Gizmo.getPlugin(Gizmo.class);


    @EventHandler
    public void onCommandItemClick(InventoryClickEvent e) {

        if (e.getView().getTitle().equals(screenTitle())) {

            Player p = (Player) e.getWhoClicked();

            ItemStack clickedItem = e.getCurrentItem();
            int rawSlot = e.getRawSlot();

            if (Gizmo.getInstance().getScreensConfigurationCore().getSectionKeys("Items") != null) {
                for (String key : Objects.requireNonNull(Gizmo.getInstance().getScreensConfigurationCore().getSectionKeys("Items"))) {
                    if (Gizmo.getInstance().getScreensConfigurationCore().getInteger("Items." + key + ".slot") == rawSlot) {
                        if (Gizmo.getInstance().getScreensConfigurationCore().getString("Items." + key + ".commands") != null) {
                            if (Gizmo.getInstance().getScreensConfigurationCore().getString("Items." + key + ".close-on-click").equals("true")) {
                                p.closeInventory();
                            }
                            for (String command : Gizmo.getInstance().getScreensConfigurationCore().getStringList("Items." + key + ".commands")) {
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
                                    Logger.log(LoggingLevel.ERROR, "\"" + key + "\"" + " (screens.yml) has a command with an invalid format.");
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
