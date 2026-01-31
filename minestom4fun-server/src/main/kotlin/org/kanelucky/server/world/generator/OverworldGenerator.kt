package org.kanelucky.world.generator

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator

/**
 * @author Kanelucky
 */
class OverworldGenerator : Generator {
    override fun generate(unit: GenerationUnit) {
        unit.modifier().fillHeight(-64, 0, Block.DEEPSLATE)
        unit.modifier().fillHeight(0, 60, Block.STONE)
        unit.modifier().fillHeight(60, 63, Block.DIRT)
        unit.modifier().fillHeight(63, 64, Block.GRASS_BLOCK)
    }
}