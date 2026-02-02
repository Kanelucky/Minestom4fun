package org.kanelucky.server.world.generator.SuperFlatGenerator.preset

import org.kanelucky.server.world.generator.SuperFlatGenerator.Layer
import org.kanelucky.server.world.generator.SuperFlatGenerator.SuperFlatPreset

import net.minestom.server.instance.block.Block
import net.minestom.server.world.biome.Biome

/**
 * @author Kanelucky
 */
class Void : SuperFlatPreset {

    override val name = "SnowyKingdom"

    override val layers = listOf(
        Layer(-59, -60, Block.AIR),
    )

    override val biome = Biome.THE_VOID
}