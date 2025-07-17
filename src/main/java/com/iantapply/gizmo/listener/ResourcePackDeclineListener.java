package com.iantapply.gizmo.listener;

import com.iantapply.gizmo.GizmoRenewed;
import com.iantapply.gizmo.data.ChatTranslate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class ResourcePackDeclineListener implements Listener {

    @EventHandler
    public void onPackLoad(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();

        if (Objects.equals(GizmoRenewed.getInstance().getConfig().getString("kick-on-decline"), "true")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disablePotionEffects(p);
            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disablePotionEffects(p);
                p.kickPlayer(ChatTranslate.translate(null, Objects.requireNonNull(GizmoRenewed.getInstance().getConfig().getString("messages.kick-on-decline")).replace(",", "\n").replace("[", "").replace("]", "")));
            }
        } else if (Objects.equals(GizmoRenewed.getInstance().getConfig().getString("kick-on-decline"), "false")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disablePotionEffects(p);
            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disablePotionEffects(p);
                for (String msg : GizmoRenewed.getInstance().getConfig().getStringList("messages.no-pack-loaded")) {
                    p.sendMessage(ChatTranslate.translate(null, msg));
                }
            }
        }
    }

    private void disablePotionEffects(Player p) {

        if (Objects.equals(GizmoRenewed.getInstance().getConfig().getString("clear-effects-on-load"), "false")){
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            return;
        }

        for (PotionEffectType effect : PotionEffectType.values()) {
            if (p.hasPotionEffect(effect)) {
                p.removePotionEffect(effect);
            }
        }
    }
}
