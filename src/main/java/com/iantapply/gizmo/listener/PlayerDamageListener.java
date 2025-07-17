package com.iantapply.gizmo.listener;

import com.iantapply.gizmo.GizmoRenewed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;

import java.util.Objects;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByBlockEvent e) {
        Entity entity = e.getEntity();
        if (Objects.equals(GizmoRenewed.getInstance().getConfig().getString("player-invulnerable-during-load"), "true")) {
            if (entity instanceof Player) {
                Player p = (Player) e.getEntity();
                if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId()) != null) {
                    if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
