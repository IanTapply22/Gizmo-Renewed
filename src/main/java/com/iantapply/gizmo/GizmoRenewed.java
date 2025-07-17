package com.iantapply.gizmo;

import com.iantapply.gizmo.command.CommandCore;
import com.iantapply.gizmo.configuration.ConfigurationCore;
import com.iantapply.gizmo.configuration.PluginConfiguration;
import com.iantapply.gizmo.listener.ListenerCore;
import com.iantapply.gizmo.logger.Logger;
import com.iantapply.gizmo.metrics.Metrics;
import com.iantapply.gizmo.metrics.UpdateChecker;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

@Getter
public final class GizmoRenewed extends JavaPlugin implements Listener {

    @Getter
    public static GizmoRenewed instance;
    public CommandCore commandCore;
    public ListenerCore listenerCore;
    public ConfigurationCore configConfigurationCore;
    public ConfigurationCore screensConfigurationCore;
    public ConfigurationCore messagesConfigurationCore;
    public UpdateChecker updateChecker;

    public HashMap<UUID, String> playerTracker = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        Logger.logInitialization();

        this.configConfigurationCore = new ConfigurationCore(this, PluginConfiguration.PLUGIN_CONFIG_FILE);
        this.configConfigurationCore.initialize();

        this.screensConfigurationCore = new ConfigurationCore(this, PluginConfiguration.SCREENS_CONFIG_FILE);
        this.screensConfigurationCore.initialize();

        this.messagesConfigurationCore = new ConfigurationCore(this, PluginConfiguration.MESSAGES_CONFIG_FILE);
        this.messagesConfigurationCore.initialize();

        this.commandCore = new CommandCore(this);
        this.commandCore.initialize();
        this.commandCore.registerCommands();

        this.listenerCore = new ListenerCore();
        this.listenerCore.initialize();

        new Metrics(this, PluginConfiguration.BSTATS_PLUGIN_ID);

        this.updateChecker = new UpdateChecker(this, PluginConfiguration.SPIGOTMC_PLUGIN_ID);
        this.updateChecker.check();

        Logger.logStartup();
    }
    @Override
    public void onDisable() {
        this.playerTracker.clear();

        Logger.logShutdown();
    }
}
