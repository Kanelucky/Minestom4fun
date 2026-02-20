package org.kanelucky.server.world.generator.decoration

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import org.kanelucky.server.world.generator.noise.TerrainNoise

import kotlin.random.Random

/**
 * @author Kanelucky
 */
class VegetationDecorator {

    fun decorate(unit: GenerationUnit) {

        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()
        val modifier = unit.modifier()

        for (x in start.blockX() until end.blockX()) {
            for (z in start.blockZ() until end.blockZ()) {

                val surfaceY = TerrainNoise.terrainHeight(x, z) - 1

                if (surfaceY <= start.blockY() || surfaceY >= end.blockY())
                    continue

                if (Random.nextFloat() < 0.12f)
                    modifier.setBlock(x, surfaceY + 1, z, Block.SHORT_GRASS)

                if (Random.nextFloat() < 0.02f)
                    modifier.setBlock(x, surfaceY + 1, z, Block.POPPY)

                if (Random.nextFloat() < 0.01f)
                    TreeGenerator.generate(unit, x, surfaceY + 1, z)
            }
        }
    }
}