package org.kanelucky.server.world.generator.terrain

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import org.kanelucky.server.world.generator.WorldConstants.CAVE_MIN_Y
import org.kanelucky.server.world.generator.WorldConstants.CAVE_SURFACE_BUFFER
import org.kanelucky.server.world.generator.WorldConstants.CAVE_THRESHOLD

import org.kanelucky.server.world.generator.noise.TerrainNoise

/**
 * @author Kanelucky
 */
class CaveCarver {

    fun carve(
        unit: GenerationUnit,
        heights: Array<IntArray>,
        baseX: Int,
        baseZ: Int
    ) {
        val modifier = unit.modifier()

        for (lx in 0 until 16) {
            for (lz in 0 until 16) {
                val wx = baseX + lx
                val wz = baseZ + lz
                val surfaceY = heights[lx][lz]
                val maxY = (surfaceY - CAVE_SURFACE_BUFFER).coerceAtMost(unit.absoluteEnd().blockY())

                if (maxY <= CAVE_MIN_Y) continue

                for (y in maxOf(unit.absoluteStart().blockY(), CAVE_MIN_Y) until maxY) {
                    if (TerrainNoise.cave(wx, y, wz) > CAVE_THRESHOLD) {
                        modifier.setBlock(wx, y, wz, Block.AIR)
                    }
                }
            }
        }
    }
}