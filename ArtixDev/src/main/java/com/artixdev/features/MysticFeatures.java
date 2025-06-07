package com.artixdev.features;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;

import java.util.Random;

public class MysticFeatures {

    // Helper method to safely set a block, ensuring it's a replaceable type
    private static boolean canReplace(Block block) {
        return block.isPassable() || Tag.LEAVES.isTagged(block.getType()) || Tag.LOGS.isTagged(block.getType()) || block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK;
    }

    public static boolean generateAncientDarkOak(World world, Random random, int baseX, int baseY, int baseZ) {
        // Check if the ground is suitable (e.g., grass or dirt)
        Material groundType = world.getBlockAt(baseX, baseY - 1, baseZ).getType();
        if (groundType != Material.GRASS_BLOCK && groundType != Material.DIRT && groundType != Material.MOSS_BLOCK && groundType != Material.PODZOL) {
            // Don't generate on unsuitable ground like stone, unless it's part of Mystic Grove's design
            // For now, let's assume it needs a soil-like block.
            // This check might be better handled by the populator calling this.
            // return false;
        }

        // Define tree parameters
        int minTrunkHeight = 7;
        int maxTrunkHeight = 11;
        int trunkHeight = minTrunkHeight + random.nextInt(maxTrunkHeight - minTrunkHeight + 1);

        // Simple 2x2 trunk for now
        // Clear area for trunk and ensure space
        for (int y = 0; y < trunkHeight + 5; y++) { // Check a bit above max leaf height
            for (int dx = -1; dx <= 2; dx++) { // Check a 3x3 area around the 2x2 trunk base
                for (int dz = -1; dz <= 2; dz++) {
                    if (y < trunkHeight) { // Trunk area
                         if (dx >=0 && dx <=1 && dz >=0 && dz <=1) continue; // Skip the trunk itself for now
                    }
                    // For leaf area or around trunk, ensure it's mostly air or replaceable
                    Block currentBlock = world.getBlockAt(baseX + dx, baseY + y, baseZ + dz);
                    if (y > 2 && !currentBlock.isPassable() && !Tag.LEAVES.isTagged(currentBlock.getType())) {
                        // If there's a solid obstruction in the leaf/upper trunk area, abort
                        // plugin.getLogger().info("Obstruction found for ancient dark oak at " + (baseX+dx) + "," + (baseY+y) + "," + (baseZ+dz));
                        // return false;
                    }
                }
            }
        }


        // Generate 2x2 trunk
        for (int y = 0; y < trunkHeight; y++) {
            for (int dx = 0; dx <= 1; dx++) {
                for (int dz = 0; dz <= 1; dz++) {
                    world.getBlockAt(baseX + dx, baseY + y, baseZ + dz).setType(Material.DARK_OAK_LOG);
                }
            }
        }

        // Generate leaves - simple canopy for now
        // Canopy starts a bit below the top of the trunk and extends upwards
        int canopyBaseY = baseY + trunkHeight - 3;
        int canopyHeight = 5 + random.nextInt(3); // Height of the leaf ball
        int canopyRadius = 4 + random.nextInt(2);  // Max radius of leaves from trunk center

        for (int y = canopyBaseY; y < canopyBaseY + canopyHeight; y++) {
            for (int dx = -canopyRadius; dx <= canopyRadius +1; dx++) { // +1 because trunk is 2x2
                for (int dz = -canopyRadius; dz <= canopyRadius +1; dz++) {
                    // Calculate distance from the center of the canopy (approx center of 2x2 trunk)
                    double dist = Math.sqrt(Math.pow(dx - 0.5, 2) + Math.pow(dz - 0.5, 2));

                    // Create a roughly spherical/ellipsoidal canopy shape
                    if (dist <= canopyRadius) {
                        // Make it denser towards the center and thinner at edges
                        if (random.nextDouble() > (dist / (canopyRadius + 1.0)) - 0.2 ) { // more chance to place if closer to center
                            Block leafBlock = world.getBlockAt(baseX + dx, y, baseZ + dz);
                            if (leafBlock.getType() == Material.AIR || Tag.LEAVES.isTagged(leafBlock.getType())) {
                                leafBlock.setType(Material.DARK_OAK_LEAVES);
                                if (leafBlock.getBlockData() instanceof Leaves) {
                                    Leaves leaves = (Leaves) leafBlock.getBlockData();
                                    leaves.setPersistent(true); // Prevent decay if they are far from logs
                                    leafBlock.setBlockData(leaves);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add a few more "branch-like" leaf clusters extending outwards (optional, simple version)
        // This part can be greatly expanded to make more realistic branches with logs.
        // For now, the main canopy is generated above.

        // Simple root-like flares at the base (optional)
        // world.getBlockAt(baseX - 1, baseY, baseZ).setType(Material.DARK_OAK_LOG);
        // world.getBlockAt(baseX + 2, baseY, baseZ).setType(Material.DARK_OAK_LOG);
        // world.getBlockAt(baseX, baseY, baseZ - 1).setType(Material.DARK_OAK_LOG);
        // world.getBlockAt(baseX, baseY, baseZ + 2).setType(Material.DARK_OAK_LOG);


        return true; // Successfully generated (or attempted to)
    }
}
