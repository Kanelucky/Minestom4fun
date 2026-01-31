package org.kanelucky.world.generator.SuperFlatGenerator.preset

import org.kanelucky.world.generator.SuperFlatGenerator.Layer
import org.kanelucky.world.generator.SuperFlatGenerator.SuperFlatPreset

import net.minestom.server.instance.block.Block
import net.minestom.server.world.biome.Biome

/**
 * @author Kanelucky
 */
class ClassicFlat : SuperFlatPreset {

    override val name = "ClassicFlat"

    override val layers = listOf(
        Layer(-61, -60, Block.GRASS_BLOCK),
        Layer(-63, -61, Block.DIRT),
        Layer(-64, -63, Block.BEDROCK)
    )

    override val biome = Biome.PLAINS

}