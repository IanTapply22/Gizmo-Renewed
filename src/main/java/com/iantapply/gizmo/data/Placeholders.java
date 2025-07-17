package com.iantapply.gizmo.data;

import com.iantapply.gizmo.GizmoRenewed;
import org.bukkit.ChatColor;

public class Placeholders {

    public static String gizmoPrefix() {
        return "#ee0000[Gizmo] ";
    }
    public static String gizmoPrefixNoDec() {
        return "#ee0000Gizmo";
    }


    public static String shift48() {
        return GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.shift-48");
    }
    public static String shift1013() {
        return GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.shift-1013");
    }
    public static String shift1536() {
        return GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.shift-1536");
    }

    public static String screenTitle() {
        return GizmoRenewed.getInstance().getConfigConfigurationCore().getString("background-color") +
                shift1013() + GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.background") +
                shift1536() + ChatColor.WHITE + GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.welcome-screen");
    }
    public static String screenTitleFirstJoin() {
        return GizmoRenewed.getInstance().getConfigConfigurationCore().getString("background-color") +
                shift1013() + GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.first-join-background") +
                shift1536() + ChatColor.WHITE + GizmoRenewed.getInstance().getScreensConfigurationCore().getString("Unicodes.first-join-welcome-screen");
    }
}
