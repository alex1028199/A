package com.artixdev.generator;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class NetherGenerator extends ChunkGenerator {

    private SimplexOctaveGenerator netherNoiseGenerator;
    private static final int LAVA_SEA_LEVEL = 31;

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        if (netherNoiseGenerator == null) {
            netherNoiseGenerator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 8);
            netherNoiseGenerator.setScale(0.015);
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                for (int y = worldInfo.getMinHeight() + 1; y < worldInfo.getMaxHeight(); y++) {
                    double density = netherNoiseGenerator.noise(worldX, y, worldZ, 0.5, 0.5, true);
                    if (density < 0.1) {
                        chunkData.setBlock(x, y, z, Material.NETHERRACK);
                    } else if (y <= LAVA_SEA_LEVEL && density < 0.15) {
                        chunkData.setBlock(x, y, z, Material.LAVA);
                    }
                }
                chunkData.setBlock(x, worldInfo.getMinHeight(), z, Material.BEDROCK);
                chunkData.setBlock(x, worldInfo.getMaxHeight() - 1, z, Material.BEDROCK);
            }
        }
    }

    // Enable vanilla features for Nether
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
