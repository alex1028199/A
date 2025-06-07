package com.artixdev.generator;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class EndGenerator extends ChunkGenerator {

    private SimplexOctaveGenerator endIslandNoise;
    private static final double MAIN_ISLAND_RADIUS = 48.0;
    private static final int ISLAND_CENTER_X = 0;
    private static final int ISLAND_CENTER_Z = 0;
    private static final int ISLAND_SURFACE_Y = 60;

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        if (endIslandNoise == null) {
            endIslandNoise = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 8);
            endIslandNoise.setScale(0.02);
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;

                double distanceSquared = Math.pow(worldX - ISLAND_CENTER_X, 2) + Math.pow(worldZ - ISLAND_CENTER_Z, 2);

                if (distanceSquared < Math.pow(MAIN_ISLAND_RADIUS + 20, 2)) {
                    double noiseVal = endIslandNoise.noise(worldX, worldZ, 0.5, 0.3, true);
                    int baseHeight = ISLAND_SURFACE_Y + (int)(noiseVal * 10);

                    if (distanceSquared < Math.pow(MAIN_ISLAND_RADIUS + (noiseVal * 5), 2) ) {
                        for (int y = worldInfo.getMinHeight(); y < baseHeight; y++) {
                            chunkData.setBlock(x, y, z, Material.END_STONE);
                        }
                    }
                }
            }
        }
    }

    // Enable vanilla features for The End
    @Override
    public boolean shouldGenerateNoise() { return true; }

    @Override
    public boolean shouldGenerateSurface() { return true; }

    @Override
    public boolean shouldGenerateBedrock() { return false; } // The End doesn't have typical bedrock layers.

    @Override
    public boolean shouldGenerateCaves() { return false; } // No traditional caves in The End main island.

    @Override
    public boolean shouldGenerateDecorations() { return true; } // For Chorus Trees, End Gateways.

    @Override
    public boolean shouldGenerateStructures() { return true; } // For End Cities, obsidian pillars, portal complex.

    @Override
    public boolean shouldGenerateMobs() { return true; }
}
