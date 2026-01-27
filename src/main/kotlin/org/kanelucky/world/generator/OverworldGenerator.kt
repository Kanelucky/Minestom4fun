package org.kanelucky.world.generator

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator

class OverworldGenerator : Generator {
    override fun generate(unit: GenerationUnit) {
        unit.modifier().fillHeight(0, 100, Block.TNT)
        unit.modifier().fillHeight(101, 200, Block.GRASS_BLOCK)
    }
}