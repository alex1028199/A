package com.artixdev;

import com.artixdev.generator.EndGenerator;
import com.artixdev.generator.NetherGenerator;
import com.artixdev.generator.OverworldGenerator; // Ensure this is the correct path
import com.artixdev.generator.biome.ArtixOverworldBiomeProvider; // Added
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider; // Added
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ArtixDev extends JavaPlugin {
    private FileConfiguration config;
    private ArtixOverworldBiomeProvider artixOverworldBiomeProvider; // Added

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        artixOverworldBiomeProvider = new ArtixOverworldBiomeProvider(this); // Initialize

        getLogger().info("ArtixDev plugin has been enabled!");
        getLogger().info("Overworld generation enabled: " + config.getBoolean("worlds.overworld.enabled", true));
        getLogger().info("Nether generation enabled: " + config.getBoolean("worlds.nether.enabled", true));
        getLogger().info("End generation enabled: " + config.getBoolean("worlds.end.enabled", true));
        getLogger().info("Custom Overworld Biomes enabled: " + config.getBoolean("features.overworld.custom_biomes.enabled", true)); // New config
    }

    @Override
    public void onDisable() {
        getLogger().info("ArtixDev plugin has been disabled!");
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        // Ensure config is loaded, though it should be by onEnable
        if (config == null) { config = getConfig(); }

        getLogger().info("ArtixDev: Requesting generator for world: " + worldName);
        if (worldName.equalsIgnoreCase("world")) {
            if (config.getBoolean("worlds.overworld.enabled", true)) {
                getLogger().info("ArtixDev: Providing OverworldGenerator for " + worldName);
                // Pass the BiomeProvider if custom biomes are enabled
                BiomeProvider biomeProvider = null;
                if (config.getBoolean("features.overworld.custom_biomes.enabled", true)) {
                    getLogger().info("ArtixDev: Using ArtixOverworldBiomeProvider for Overworld.");
                    biomeProvider = artixOverworldBiomeProvider;
                } else {
                    getLogger().info("ArtixDev: Using default BiomeProvider for Overworld.");
                    // Let OverworldGenerator constructor handle null BiomeProvider (meaning vanilla)
                }
                return new OverworldGenerator(this, biomeProvider); // Pass plugin instance and biomeProvider
            }
            getLogger().info("ArtixDev: Custom Overworld generation disabled in config.");
        } else if (worldName.equalsIgnoreCase("world_nether")) {
            // ... (NetherGenerator instantiation, potentially with its own BiomeProvider later)
             if (config.getBoolean("worlds.nether.enabled", true)) {
                return new com.artixdev.generator.NetherGenerator();
            }
        } else if (worldName.equalsIgnoreCase("world_the_end")) {
            // ... (EndGenerator instantiation)
            if (config.getBoolean("worlds.end.enabled", true)) {
                return new com.artixdev.generator.EndGenerator();
            }
        }
        return null;
    }

    // Method to access the custom biome provider if needed by other components
    public ArtixOverworldBiomeProvider getArtixOverworldBiomeProvider() {
        return artixOverworldBiomeProvider;
    }
}
