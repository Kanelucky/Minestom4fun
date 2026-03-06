package org.kanelucky.api.java.world.spawner;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.biome.Biome;

import java.util.Set;

/**
 * @author Kanelucky
 */
public interface SpawnRule {
    int getWeight();
    int getMinGroupSize();
    int getMaxGroupSize();
    Set<RegistryKey<Biome>> getValidBiomes();

    boolean canSpawn(Instance instance, Point pos);
    Entity createEntity();

    default boolean isValidSpawnPos(Instance instance, Point pos) {
        var chunk = instance.getChunkAt(pos);
        if (chunk == null || !chunk.isLoaded()) return false;

        var ground = instance.getBlock(pos.blockX(), pos.blockY() - 1, pos.blockZ());
        if (!ground.isSolid()) return false;

        var at = instance.getBlock(pos.blockX(), pos.blockY(), pos.blockZ());
        var above = instance.getBlock(pos.blockX(), pos.blockY() + 1, pos.blockZ());
        if (at.isSolid() || above.isSolid()) return false;

        int skyLight = instance.getSkyLight(pos.blockX(), pos.blockY(), pos.blockZ());
        if (skyLight < 9) return false;

        var biome = instance.getBiome(pos.blockX(), pos.blockY(), pos.blockZ());
        return getValidBiomes().contains(biome);
    }
}
