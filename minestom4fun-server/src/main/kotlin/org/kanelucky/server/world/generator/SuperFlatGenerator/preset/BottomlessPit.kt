package org.kanelucky.server.world.generator.SuperFlatGenerator.preset

import org.kanelucky.server.world.generator.SuperFlatGenerator.Layer
import org.kanelucky.server.world.generator.SuperFlatGenerator.SuperFlatPreset

import net.minestom.server.instance.block.Block
import net.minestom.server.world.biome.Biome

/**
 * @author Kanelucky
 */
class BottomlessPit : SuperFlatPreset {

    override val name = "BottomlessPit"

    override val layers = listOf(
        Layer(-59, -58, Block.GRASS_BLOCK),
        Layer(-62, -59, Block.DIRT),
        Layer(-64, -62, Block.COBBLESTONE)
    )

    override val biome = Biome.PLAINS

}