package com.artixdev;

import com.artixdev.generator.EndGenerator; // Added import
import com.artixdev.generator.NetherGenerator;
import com.artixdev.generator.OverworldGenerator;
import org.bukkit.configuration.file.FileConfiguration; // Added import
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ArtixDev extends JavaPlugin {

    private FileConfiguration config; // Added

    @Override
    public void onEnable() {
        getLogger().info("ArtixDev plugin has been enabled!");

        // Load and save default config
        saveDefaultConfig(); // This saves config.yml from resources if not present
        config = getConfig(); // Load the config

        // You can add messages here to confirm config loading if desired
        getLogger().info("Overworld generation enabled: " + config.getBoolean("worlds.overworld.enabled", true));
        getLogger().info("Nether generation enabled: " + config.getBoolean("worlds.nether.enabled", true));
        getLogger().info("End generation enabled: " + config.getBoolean("worlds.end.enabled", true));
    }

    @Override
    public void onDisable() {
        getLogger().info("ArtixDev plugin has been disabled!");
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        // Ensure config is loaded, though it should be by onEnable
        if (config == null) {
            config = getConfig();
        }

        getLogger().info("ArtixDev: Requesting default world generator for world: " + worldName + " (ID: " + id + ")");

        if (worldName.equalsIgnoreCase("world")) {
            if (config.getBoolean("worlds.overworld.enabled", true)) {
                getLogger().info("ArtixDev: Providing OverworldGenerator for " + worldName);
                return new OverworldGenerator();
            }
            getLogger().info("ArtixDev: Custom Overworld generation disabled in config.");
        } else if (worldName.equalsIgnoreCase("world_nether")) {
            if (config.getBoolean("worlds.nether.enabled", true)) {
                getLogger().info("ArtixDev: Providing NetherGenerator for " + worldName);
                return new NetherGenerator();
            }
            getLogger().info("ArtixDev: Custom Nether generation disabled in config.");
        } else if (worldName.equalsIgnoreCase("world_the_end")) {
            if (config.getBoolean("worlds.end.enabled", true)) {
                getLogger().info("ArtixDev: Providing EndGenerator for " + worldName);
                return new EndGenerator();
            }
            getLogger().info("ArtixDev: Custom End generation disabled in config.");
        }

        // If no specific condition met or generation is disabled for the world
        return null;
    }
}
