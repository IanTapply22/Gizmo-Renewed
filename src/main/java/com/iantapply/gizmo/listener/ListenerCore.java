package com.iantapply.gizmo.listener;

import com.iantapply.gizmo.GizmoRenewed;
import org.bukkit.Bukkit;

/**
 * Handles all core functionality in regard to the listeners
 */
public class ListenerCore {

    /**
     * Initializes all listeners for the Gizmo plugin and registers them with the Bukkit plugin manager.
     */
    public void initialize() {
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new ItemClickListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new ItemPickupListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerItemDamageListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new ResourcePackDeclineListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new RestoreInventoryListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new ScreenAdvanceListener(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new ScreenDisplayHandler(), GizmoRenewed.getInstance());
        Bukkit.getPluginManager().registerEvents(new SlotClickListener(), GizmoRenewed.getInstance());
    }
}
