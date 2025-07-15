package com.iantapply.gizmo.listeners;

import com.iantapply.gizmo.Gizmo;
import com.iantapply.gizmo.data.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.iantapply.gizmo.data.Placeholders.*;

public class PlayerScreening implements Listener {


    static Gizmo plugin = Gizmo.getPlugin(Gizmo.class);
    public static HashMap<UUID, ItemStack[]> saveInv = new HashMap<>();
    public static HashMap<UUID, Boolean> playersScreenActive = new HashMap<>();

    // Resource pack status event
    @EventHandler
    public boolean onPackAccept(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();
        // Play a configured sound when the pack is loaded
        if (Objects.equals(plugin.getConfig().getString("sound-on-pack-load.enable"), "true")) {
            p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound-on-pack-load.sound")), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-pack-load.volume"))), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-pack-load.pitch"))));
        }
        switch (e.getStatus()) {
            case ACCEPTED:
                // Display the background unicode during the delay
                if (plugin.getConfig().getString("delay-background").equals("true")) {
                    p.sendTitle(Gizmo.getInstance().getConfigConfigurationCore().getString("background-color") + Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.background"), "", 0, 999999, 0);
                }
                break;
            case SUCCESSFULLY_LOADED:
                // Play a configured sound when the pack is loaded
                if (Objects.equals(plugin.getConfig().getString("sound-on-pack-load.enable"), "true")) {
                    p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound-on-pack-load.sound")), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-pack-load.volume"))), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-pack-load.pitch"))));
                }
                // Display first time welcome screen
                if (!p.hasPlayedBefore()) {
                    if (Gizmo.getInstance().getScreensConfigurationCore().getBoolean("enable-first-join-welcome-screen")) {
                        if (Gizmo.getInstance().getPlayerTracker().get(p.getUniqueId()) == null) {
                            Gizmo.getInstance().playerTracker.put(p.getUniqueId(), String.valueOf(1));
                            welcomeScreenInitial(p);
                        }
                        return false;
                    }
                }
                // Display the screen once per restart
                if (Gizmo.getInstance().getScreensConfigurationCore().getBoolean("once-per-restart")) {
                    // Check if the player has already seen the screen this server session
                    if (Gizmo.getInstance().getPlayerTracker().get(p.getUniqueId()) == null) {
                        Gizmo.getInstance().playerTracker.put(p.getUniqueId(), String.valueOf(1));
                        welcomeScreen(p);
                    }
                } else if (!Gizmo.getInstance().getScreensConfigurationCore().getBoolean("once-per-restart")) {
                    welcomeScreen(p);
                }
                break;
            case DECLINED:
            case FAILED_DOWNLOAD:
                // Debug mode check; if enabled it will still send the player the welcome screen
                if (plugin.getConfig().getString("debug-mode").equalsIgnoreCase("true")) {
                    p.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "#acb5bfNo server resource pack detected and/or debug mode is enabled."));
                    p.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "#acb5bfSending welcome screen..."));
                    welcomeScreen(p);
                } else {
                    p.removePotionEffect(PotionEffectType.BLINDNESS);
                    if (Gizmo.getInstance().getMessagesConfigurationCore().getString("no-pack-loaded").equals("[]")) {
                        return false;
                    } else {
                        for (String msg : Gizmo.getInstance().getMessagesConfigurationCore().getStringList("no-pack-loaded")) {
                            p.sendMessage(Utilities.chatTranslate(msg));
                        }
                    }
                }
                break;
        }
        return false;
    }

    // Welcome screen
    public static void welcomeScreen(Player e) {
        playersScreenActive.put(e.getUniqueId(), true);
        saveInv.put(e.getPlayer().getUniqueId(), e.getPlayer().getInventory().getContents());
        e.getPlayer().getInventory().clear();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (Gizmo.getInstance().getScreensConfigurationCore().getBoolean("enable-welcome-screen")) {
                InventoryView screen = e.getPlayer().openInventory(
                        plugin.getServer().createInventory(null, 54, screenTitle())
                );

                if (screen == null) {
                    System.out.println("Error: Unable to open inventory for player " + e.getName() +
                            ". The screen may not be configured correctly. Title: " + screenTitle());
                    return;
                }

                ConfigurationSection itemsSection = Gizmo.getInstance().getScreensConfigurationCore().getSection("Items");
                if (itemsSection != null) {
                    for (String key : itemsSection.getKeys(false)) {
                        ConfigurationSection keySection = itemsSection.getConfigurationSection(key);
                        if (keySection == null) continue;
                        int slot = keySection.getInt("slot");
                        Material material = Material.matchMaterial(keySection.getString("material"));
                        if (material == null) continue;
                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        if (meta == null) continue;

                        List<String> lore = keySection.getStringList("lore");
                        if (lore != null && !lore.isEmpty()) {
                            lore.replaceAll(Utilities::chatTranslate);
                            meta.setLore(lore);
                        }

                        if (Objects.equals(keySection.getString("hide-flags"), "true")) {
                            meta.addItemFlags(
                                    ItemFlag.HIDE_ATTRIBUTES,
                                    ItemFlag.HIDE_DESTROYS,
                                    ItemFlag.HIDE_ENCHANTS,
                                    ItemFlag.HIDE_PLACED_ON,
                                    ItemFlag.HIDE_UNBREAKABLE
                            );
                        }

                        meta.setCustomModelData(keySection.getInt("custom-model-data"));
                        meta.setDisplayName(Utilities.chatTranslate(keySection.getString("name")));
                        item.setItemMeta(meta);

                        placeItemInUnifiedSlot(screen, slot, item);
                    }
                }
            }
        }, plugin.getConfig().getInt("delay"));
    }

    public static void placeItemInUnifiedSlot(InventoryView screen, int slot, ItemStack item) {
        screen.setItem(slot, item);
    }

    // Welcome screen first join
    public void welcomeScreenInitial(Player e) {
        // Store the player's ID and set the screen to active
        playersScreenActive.put(e.getUniqueId(), true);
        // Store and clear the player's inventory
        saveInv.put(e.getPlayer().getUniqueId(), e.getPlayer().getInventory().getContents());
        e.getPlayer().getInventory().clear();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {

                // Begin the screen sequence
                // check if screens.yml enable-first-join-welcome-screen = true
                if (Gizmo.getInstance().getScreensConfigurationCore().getBoolean("enable-first-join-welcome-screen")) {
                    InventoryView screen = e.getPlayer().openInventory(
                            plugin.getServer().createInventory(null, 54, screenTitleFirstJoin())
                    );

                        if (screen == null) {
                            System.out.println("Error: Unable to open inventory for player " + e.getName() +
                                    ". The screen may not be configured correctly. Title: " + screenTitleFirstJoin());
                            return;
                        }

                        if (Gizmo.getInstance().getScreensConfigurationCore().getSection("First-Join-Items") != null) {
                            for (String key : Objects.requireNonNull(Gizmo.getInstance().getScreensConfigurationCore().getSectionKeys("First-Join-Items"))) {
                                ConfigurationSection keySection = Objects.requireNonNull(Gizmo.getInstance().getScreensConfigurationCore().getSection("First-Join-Items." + key));
                                assert keySection != null;
                                int slot = keySection.getInt("slot");
                                ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(keySection.getString("material")))));
                                ItemMeta meta = item.getItemMeta();
                                if (Gizmo.getInstance().getScreensConfigurationCore().getString("First-Join-Items." + key + ".lore") != null) {
                                    List<String> lore = keySection.getStringList("lore");
                                    for (int i = 0; i < lore.size(); i++) {
                                        lore.set(i, Utilities.chatTranslate(lore.get(i)));
                                    }
                                    assert meta != null;
                                    meta.setLore(lore);
                                }
                                assert meta != null;
                                if (Objects.equals(keySection.getString("hide-flags"), String.valueOf(true))) {
                                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                }
                                meta.setCustomModelData(keySection.getInt("custom-model-data"));
                                meta.setDisplayName(Utilities.chatTranslate(keySection.getString("name")));
                                item.setItemMeta(meta);

                                placeItemInUnifiedSlot(screen, slot, item);
                            }
                        }
                }
            }
        }, plugin.getConfig().getInt("delay"));
    }
}
