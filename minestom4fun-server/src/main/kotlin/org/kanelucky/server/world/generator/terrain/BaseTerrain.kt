package org.kanelucky.server.world.generator.terrain

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import org.kanelucky.server.world.generator.WorldConstants.WATER_LEVEL
import org.kanelucky.server.world.generator.noise.TerrainNoise

/**
 * @author Kanelucky
 */
class BaseTerrain {

    fun build(unit: GenerationUnit): Array<IntArray> {

        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()
        val modifier = unit.modifier()

        val baseX = start.blockX()
        val baseZ = start.blockZ()
        val heights = Array(16) { IntArray(16) }

        for (x in start.blockX() until end.blockX()) {
            for (z in start.blockZ() until end.blockZ()) {

                val height = TerrainNoise.terrainHeight(x, z)
                heights[x - baseX][z - baseZ] = height

                for (y in start.blockY() until end.blockY()) {
                    val block = when {
                        y < height - 4  -> Block.STONE
                        y < height - 1  -> Block.DIRT
                        y == height - 1 -> if (height <= WATER_LEVEL + 1) Block.SAND else Block.GRASS_BLOCK
                        y < WATER_LEVEL -> if (height < WATER_LEVEL - 2) Block.WATER else Block.SAND
                        else            -> null
                    }
                    if (block != null) modifier.setBlock(x, y, z, block)
                }
            }
        }

        return heights
    }
}