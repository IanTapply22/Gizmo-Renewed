package com.iantapply.gizmo.listener;

import com.iantapply.gizmo.GizmoRenewed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Check and give blindness effect
        if (Objects.equals(GizmoRenewed.getInstance().getConfig().getString("blindness-during-prompt"), "true")) {
            e.getPlayer().addPotionEffect(PotionEffectType.BLINDNESS.createEffect(999999, 1));
        }
    }
}
