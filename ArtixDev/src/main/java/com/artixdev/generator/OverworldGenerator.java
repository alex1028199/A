package com.artixdev.generator;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class OverworldGenerator extends ChunkGenerator {

    private SimplexOctaveGenerator noiseGenerator;

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
                double noise = noiseGenerator.noise(worldX, worldZ, 0.5, 0.5, true);
                int height = (int) (64 + noise * 30);
                height = Math.max(worldInfo.getMinHeight() + 1, Math.min(height, worldInfo.getMaxHeight() -1));

                for (int y = worldInfo.getMinHeight(); y < height; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                if (height < worldInfo.getMaxHeight()) {
                     chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
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
