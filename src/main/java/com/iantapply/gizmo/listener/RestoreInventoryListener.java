package com.iantapply.gizmo.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static com.iantapply.gizmo.data.Placeholders.screenTitle;
import static com.iantapply.gizmo.data.Placeholders.screenTitleFirstJoin;
import static com.iantapply.gizmo.listener.ScreenDisplayHandler.saveInv;

public class RestoreInventoryListener implements Listener {

    @EventHandler
    public void restoreInv(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getView().getTitle().equals(screenTitle()) || e.getView().getTitle().equals(screenTitleFirstJoin())) {
            p.getInventory().setContents(saveInv.get(p.getUniqueId()));
        }
    }
}
