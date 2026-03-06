package org.kanelucky.server.world.spawner

import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.registry.RegistryKey
import net.minestom.server.world.biome.Biome
import org.kanelucky.api.java.world.spawner.SpawnRule
import org.kanelucky.server.entity.passive.EntityPig

/**
 * @author Kanelucky
 */
object PigSpawnRule : SpawnRule {
    override fun getWeight() = 10
    override fun getMinGroupSize() = 2
    override fun getMaxGroupSize() = 4
    override fun getValidBiomes(): Set<RegistryKey<Biome>> = setOf(
        Biome.PLAINS, Biome.FOREST, Biome.MEADOW,
        Biome.TAIGA, Biome.SAVANNA, Biome.SAVANNA_PLATEAU
    )
    override fun canSpawn(instance: Instance, pos: Point) = isValidSpawnPos(instance, pos)
    override fun createEntity() = EntityPig()
}