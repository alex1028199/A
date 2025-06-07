package com.artixdev.generator;

import org.bukkit.Material;
import org.bukkit.generator.BiomeProvider; // Added
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin; // Added
import org.bukkit.util.noise.SimplexOctaveGenerator;
import com.artixdev.generator.biome.ArtixOverworldBiomeProvider; // Added

import java.util.Random;

public class OverworldGenerator extends ChunkGenerator {

    private SimplexOctaveGenerator noiseGenerator;
    private final JavaPlugin plugin; // Added
    private final ArtixOverworldBiomeProvider customBiomeProvider; // Specifically our type for convenience

    // Constructor
    public OverworldGenerator(JavaPlugin plugin, BiomeProvider biomeProvider) {
        this.plugin = plugin;
        if (biomeProvider instanceof ArtixOverworldBiomeProvider) {
            this.customBiomeProvider = (ArtixOverworldBiomeProvider) biomeProvider;
        } else {
            this.customBiomeProvider = null; // Not using our custom biome provider
        }
        // Initialize noiseGenerator here or ensure it's initialized before use in generateNoise
        // For simplicity, we'll keep its existing lazy initialization in generateNoise,
        // but it could also be initialized in this constructor using a fixed seed or world seed.
    }

    // We need to override getDefaultBiomeProvider if we want Bukkit to use it for populators
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        if (customBiomeProvider != null) {
            // plugin.getLogger().info("OverworldGenerator: Providing ArtixOverworldBiomeProvider as default.");
            return customBiomeProvider;
        }
        // plugin.getLogger().info("OverworldGenerator: Defaulting to super.getDefaultBiomeProvider().");
        return super.getDefaultBiomeProvider(worldInfo);
    }

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        if (noiseGenerator == null) {
            noiseGenerator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 8);
            noiseGenerator.setScale(0.005);
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;

                // Check if this location should be Mystic Grove
                boolean isMystic = customBiomeProvider != null && customBiomeProvider.isMysticGrove(worldInfo, worldX, 0, worldZ); // Y is not used in our current biome noise

                // Existing noise generation for height
                double heightNoise = noiseGenerator.noise(worldX, worldZ, 0.5, 0.5, true);
                int height = (int) (64 + heightNoise * 30);
                height = Math.max(worldInfo.getMinHeight() + 1, Math.min(height, worldInfo.getMaxHeight() -1));

                Material surfaceBlock = Material.GRASS_BLOCK;
                Material mainBlock = Material.STONE;

                if (isMystic) {
                    // Apply MysticGrove block palette for the main terrain
                    surfaceBlock = Material.GRASS_BLOCK;
                    mainBlock = Material.DARK_OAK_WOOD;
                }

                for (int y = worldInfo.getMinHeight(); y < height; y++) {
                    chunkData.setBlock(x, y, z, mainBlock);
                }
                if (height < worldInfo.getMaxHeight()) {
                     chunkData.setBlock(x, height, z, surfaceBlock);
                }
                chunkData.setBlock(x, worldInfo.getMinHeight(), z, Material.BEDROCK);
            }
        }
    }

    // Enable vanilla features
    @Override
    public boolean shouldGenerateNoise() { return true; }

    @Override
    public boolean shouldGenerateSurface() { return true; }

    @Override
    public boolean shouldGenerateBedrock() { return true; }

    @Override
    public boolean shouldGenerateCaves() { return true; }

    @Override
    public boolean shouldGenerateDecorations() { return true; }

    @Override
    public boolean shouldGenerateStructures() { return true; }

    @Override
    public boolean shouldGenerateMobs() { return true; }
}
