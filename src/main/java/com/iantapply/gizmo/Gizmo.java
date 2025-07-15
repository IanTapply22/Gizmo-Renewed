package com.iantapply.gizmo;

import com.iantapply.gizmo.command.CommandCore;
import com.iantapply.gizmo.configuration.ConfigurationCore;
import com.iantapply.gizmo.configuration.PluginConfiguration;
import com.iantapply.gizmo.listeners.ClickableItems;
import com.iantapply.gizmo.listeners.PlayerScreening;
import com.iantapply.gizmo.listeners.ScreenAdvance;
import com.iantapply.gizmo.listeners.ScreenHandlers;
import com.iantapply.gizmo.logger.Logger;
import com.iantapply.gizmo.metrics.Metrics;
import com.iantapply.gizmo.metrics.UpdateChecker;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

@Getter
public final class Gizmo extends JavaPlugin implements Listener {

    @Getter
    public static Gizmo instance;
    public CommandCore commandCore;
    public ConfigurationCore configConfigurationCore;
    public ConfigurationCore screensConfigurationCore;
    public ConfigurationCore messagesConfigurationCore;
    public UpdateChecker updateChecker;

    public HashMap<UUID, String> playerTracker = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        Logger.logInitialization();

        this.configConfigurationCore = new ConfigurationCore(this, "config.yml");
        this.configConfigurationCore.initialize();

        this.screensConfigurationCore = new ConfigurationCore(this, "screens.yml");
        this.screensConfigurationCore.initialize();

        this.messagesConfigurationCore = new ConfigurationCore(this, "messages.yml");
        this.messagesConfigurationCore.initialize();

        this.commandCore = new CommandCore(this);
        this.commandCore.initialize();
        this.commandCore.registerCommands();

        Bukkit.getPluginManager().registerEvents(new PlayerScreening(), this);
        Bukkit.getPluginManager().registerEvents(new ScreenHandlers(), this);
        Bukkit.getPluginManager().registerEvents(new ScreenAdvance(), this);
        Bukkit.getPluginManager().registerEvents(new ClickableItems(), this);

        new Metrics(this, PluginConfiguration.BSTATS_PLUGIN_ID);

        this.updateChecker = new UpdateChecker(this, PluginConfiguration.SPIGOTMC_PLUGIN_ID);
        this.updateChecker.check();

        Logger.logStartup();
    }
    @Override
    public void onDisable() {
        Logger.logShutdown();
    }
}
