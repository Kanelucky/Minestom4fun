package org.kanelucky.world.generator.SuperFlatGenerator.preset


import net.minestom.server.instance.block.Block
import net.minestom.server.world.biome.Biome

import org.kanelucky.api.java.world.generator.SuperFlatGenerator.Layer
import org.kanelucky.api.java.world.generator.SuperFlatGenerator.SuperFlatPreset

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