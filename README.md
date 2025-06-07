# ArtixDev Minecraft Plugin

## Overview

ArtixDev is a Spigot plugin designed to provide a basic framework for custom world generation in Minecraft. It offers modified terrain generation for all three main dimensions: the Overworld, the Nether, and The End.

This plugin primarily alters the shape and basic material composition of the terrain using noise algorithms and allows vanilla features (ores, trees, structures, etc.) to populate this custom terrain.

## Features

*   Custom terrain generation for the Overworld.
*   Custom terrain generation for the Nether, including lava seas and Netherrack landscapes.
*   Custom terrain generation for The End, focusing on the main End island.
*   Configurable: Enable or disable custom generation for each dimension individually via `config.yml`.
*   Integrates with vanilla feature generation: Allows ores, trees, structures, etc., to spawn on the custom terrain.

## Installation

1.  Ensure you have a Spigot, Paper, or compatible Bukkit-based server running Minecraft (developed against API version 1.20.1).
2.  Download the `ArtixDev-X.Y.Z.jar` file.
3.  Place the JAR file into your server's `plugins/` folder.
4.  Restart or reload your server. If reloading, ensure worlds are generated fresh or use a multiverse plugin to create new worlds with the generator.

## Configuration

After the first run, a `config.yml` file will be created in `plugins/ArtixDev/config.yml`.

```yaml
# ArtixDev Plugin Configuration

# Enable custom world generation for specific worlds.
# Set to 'false' to use vanilla generation for that world.
worlds:
  overworld:
    enabled: true
  nether:
    enabled: true
  end:
    enabled: true
```

*   **`worlds.overworld.enabled`**: Set to `true` to use ArtixDev's Overworld generator, `false` for vanilla.
*   **`worlds.nether.enabled`**: Set to `true` to use ArtixDev's Nether generator, `false` for vanilla.
*   **`worlds.end.enabled`**: Set to `true` to use ArtixDev's End generator, `false` for vanilla.

You must restart the server or regenerate/create new worlds for changes to take full effect, especially when changing a generator from disabled to enabled or vice-versa for an existing world.

## Known Issues / Considerations

*   **Biome Providers:** This plugin currently uses the default biome providers for each world. This means that while the terrain shape is custom, the biome distribution itself (e.g., where a desert vs. forest appears in the Overworld) follows vanilla logic.
*   **Feature Density:** The density and placement of vanilla features (ores, structures) might vary from standard vanilla due to the custom terrain shapes. Further fine-tuning would require more advanced generator modifications.
*   **Performance:** Custom world generation can be more resource-intensive than vanilla. Performance may vary based on server hardware and other plugins.

## Building from Source (Optional)

If you wish to build the plugin from source:
1. Clone the repository.
2. Ensure you have Java (JDK 17 or higher) and Maven installed.
3. Navigate to the project directory and run `mvn clean package`.
4. The compiled JAR will be in the `target/` directory.

---
*This plugin provides a foundational example of custom world generation.*
