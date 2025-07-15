package com.iantapply.gizmo.configuration;

public class PluginConfiguration {
    public static final String NAME = "GizmoRenewed";
    public static final String VERSION = "3.0.0";
    public static final String DEVELOPER_CREDITS = "GucciFox";
    public static final String DESCRIPTION = "";
    public static final String MAIN_CONFIG_FILE = "config.yml";
    public static final String PLUGIN_DOWNLOAD_PAGE = "";

    public static final int BSTATS_PLUGIN_ID = 26513;
    public static final int SPIGOTMC_PLUGIN_ID = 0;

    /**
     * Checks if the server and plugin are running in PaperMC mode
     * @return A boolean to determine if the server is running PaperMC
     */
    public static boolean isRunningPaper() {
        boolean isPaper = false;
        try {
            // Any other works, just the shortest I could find.
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {}

        return isPaper;
    }
}
