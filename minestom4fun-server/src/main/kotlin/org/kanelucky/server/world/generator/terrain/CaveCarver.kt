package org.kanelucky.server.world.generator.terrain

import net.minestom.server.instance.block.Block

import net.minestom.server.instance.generator.GenerationUnit
import org.kanelucky.server.world.generator.noise.TerrainNoise

/**
 * @author Kanelucky
 */
class CaveCarver {

    fun carve(unit: GenerationUnit) {

        val modifier = unit.modifier()
        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()

        for (x in start.blockX() until end.blockX()) {
            for (z in start.blockZ() until end.blockZ()) {
                for (y in start.blockY() until end.blockY()) {

                    if (y > 70) continue

                    val noise = TerrainNoise.cave(x, y, z)

                    if (noise > 0.62f) {
                        modifier.setBlock(x, y, z, Block.AIR)
                    }
                }
            }
        }
    }
}