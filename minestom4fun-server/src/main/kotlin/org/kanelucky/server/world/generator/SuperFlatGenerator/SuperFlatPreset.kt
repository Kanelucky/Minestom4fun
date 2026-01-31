package org.kanelucky.world.generator.SuperFlatGenerator

import net.minestom.server.registry.RegistryKey
import net.minestom.server.world.biome.Biome

/**
 * @author Kanelucky
 */
interface SuperFlatPreset {

    val name: String

    val layers: List<Layer>

    val biome: RegistryKey<Biome>?

}
