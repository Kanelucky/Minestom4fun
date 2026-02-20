package org.kanelucky.server.world.generator.terrain

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import org.kanelucky.server.world.generator.noise.TerrainNoise

/**
 * @author Kanelucky
 */
class BaseTerrain {

    fun build(unit: GenerationUnit) {

        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()
        val modifier = unit.modifier()

        for (x in start.blockX() until end.blockX()) {
            for (z in start.blockZ() until end.blockZ()) {

                val height = TerrainNoise.terrainHeight(x, z)

                for (y in start.blockY() until height - 4) {
                    modifier.setBlock(x, y, z, Block.STONE)
                }

                for (y in height - 4 until height - 1) {
                    modifier.setBlock(x, y, z, Block.DIRT)
                }

                modifier.setBlock(x, height - 1, z, Block.GRASS_BLOCK)
            }
        }
    }
}