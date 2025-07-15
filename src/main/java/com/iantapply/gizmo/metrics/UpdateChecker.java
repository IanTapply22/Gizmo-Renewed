package com.iantapply.gizmo.metrics;

import com.iantapply.gizmo.Gizmo;
import com.iantapply.gizmo.configuration.PluginConfiguration;
import com.iantapply.gizmo.data.Utilities;
import com.iantapply.gizmo.logger.Logger;
import com.iantapply.gizmo.logger.LoggingLevel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.function.Consumer;

import static com.iantapply.gizmo.data.Placeholders.gizmoPrefix;

/**
 *                      A utility class intended to check for updates on the SpigotMC website
 * @param plugin        The plugin instance, type org.bukkit.plugin.java.JavaPlugin
 * @param resourceId    The resource ID of the plugin on SpigotMC, type int
 */
public record UpdateChecker(Gizmo plugin, int resourceId) {

    /**
     *                  Gets the latest version of the plugin available on SpigotMC
     * @param consumer  The consumer to accept the version, type java.util.function.Consumer[java.lang.String]
     */
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin(), () -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                consumer.accept(response.body());
            } catch (Exception exception) {
                Logger.log(LoggingLevel.WARNING, "Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    /**
     * Checks for updates and notifies the user via a log to console
     * getDescription() is still used because of the usage of a plugin.yml.
     * Not planned to change
     */
    public void check() {
        new UpdateChecker(Gizmo.getInstance(), resourceId).getVersion(version -> {
            String currentVersion = Gizmo.getInstance().getDescription().getVersion();

            if (VersionChecker.isVersionLower(currentVersion, version)) {
                Logger.logUpdateNotificationConsole();
            } else if (VersionChecker.isVersionHigher(currentVersion, version)) {
                Logger.logUnreleasedVersionNotification();
            }
        });
    }
}
