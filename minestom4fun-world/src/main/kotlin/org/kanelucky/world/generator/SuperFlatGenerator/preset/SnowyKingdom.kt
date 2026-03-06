package org.kanelucky.world.generator.SuperFlatGenerator.preset

import net.minestom.server.instance.block.Block
import net.minestom.server.world.biome.Biome

import org.kanelucky.api.java.world.generator.SuperFlatGenerator.Layer

import org.kanelucky.api.java.world.generator.SuperFlatGenerator.SuperFlatPreset

/**
 * @author Kanelucky
 */
class SnowyKingdom : SuperFlatPreset {

    override val name = "SnowyKingdom"

    override val layers = listOf(
        Layer(0, 1, Block.SNOW),
        Layer(-1, 0, Block.GRASS_BLOCK),
        Layer(-4, -1, Block.DIRT),
        Layer(-63, -4, Block.STONE),
        Layer(-64, -63, Block.BEDROCK)
    )

    override val biome = Biome.SNOWY_PLAINS
}