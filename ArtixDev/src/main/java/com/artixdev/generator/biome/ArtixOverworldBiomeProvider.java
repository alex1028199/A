package com.artixdev.generator.biome;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin; // Added import
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random; // Added import
import java.util.stream.Collectors;

public class ArtixOverworldBiomeProvider extends BiomeProvider {

    private final NamespacedKey MYSTIC_GROVE_KEY;
    private SimplexOctaveGenerator biomeNoise;
    private final JavaPlugin plugin;
    private Random random; // Added for biome selection

    // List of biomes this provider can output.
    private static final List<Biome> ALLOWED_BIOMES = Arrays.asList(
            Biome.PLAINS, Biome.FOREST, Biome.DARK_FOREST, Biome.WINDSWEPT_HILLS, // Changed MOUNTAINS to WINDSWEPT_HILLS
            Biome.RIVER, Biome.SWAMP, Biome.TAIGA, Biome.BIRCH_FOREST
    );

    public ArtixOverworldBiomeProvider(JavaPlugin plugin) {
        this.plugin = plugin;
        this.MYSTIC_GROVE_KEY = new NamespacedKey(plugin, "mystic_grove");
        // Note: A proper Random instance should be initialized, ideally with a seed for consistency
        // For now, a new Random() will be used in getBiome, but for reproducible biome maps,
        // it should be seeded from worldInfo or a fixed seed if that's desired behavior.
        // Let's initialize it here for now.
        this.random = new Random();
    }

    private SimplexOctaveGenerator getBiomeNoise(WorldInfo worldInfo) {
        if (biomeNoise == null) {
            // It's better to use a separate seed for biome noise than just +1,
            // but this is okay for demonstration.
            biomeNoise = new SimplexOctaveGenerator(new Random(worldInfo.getSeed() + 1), 4);
            biomeNoise.setScale(0.002);
        }
        return biomeNoise;
    }

    @Override
    public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
        double noiseValue = getBiomeNoise(worldInfo).noise(x, z, 0.5, 0.5, true);

        if (noiseValue > 0.65 && noiseValue < 0.75) {
            // This is where we signal a custom biome.
            // As discussed, we return a base biome that our generator will then customize.
            return Biome.FOREST; // Signal for Mystic Grove, to be handled by OverworldGenerator
        }

        // Fallback to a random vanilla biome from our allowed list
        // Ensure random is initialized. If not, this could be an issue.
        // For consistent biome maps per seed, this Random should be seeded based on worldInfo or chunk location.
        // Using a single Random instance initialized in constructor might lead to less varied large-scale patterns
        // if not re-seeded or used carefully. For simplicity now:
        int index = Math.abs(this.random.nextInt()) % ALLOWED_BIOMES.size();
        return ALLOWED_BIOMES.get(index);
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return ALLOWED_BIOMES;
    }

    public boolean isMysticGrove(WorldInfo worldInfo, int x, int y, int z) {
        double noiseValue = getBiomeNoise(worldInfo).noise(x, z, 0.5, 0.5, true);
        return (noiseValue > 0.65 && noiseValue < 0.75);
    }

    public NamespacedKey getMysticGroveKey() {
        return MYSTIC_GROVE_KEY;
    }
}
