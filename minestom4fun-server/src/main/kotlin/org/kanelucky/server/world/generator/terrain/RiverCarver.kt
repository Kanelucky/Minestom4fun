package org.kanelucky.server.world.generator.terrain

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import org.kanelucky.server.world.generator.noise.RiverNoise
import org.kanelucky.server.world.generator.noise.FastNoise
import org.kanelucky.server.world.generator.noise.TerrainNoise

/**
 * @author Kanelucky
 */
class RiverCarver(private val noise: FastNoise) {

    fun carve(unit: GenerationUnit) {

        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()

        for (x in start.blockX() until end.blockX()) {
            for (z in start.blockZ() until end.blockZ()) {

                val river = kotlin.math.abs(
                    noise.fractal2D(x * 0.004, z * 0.004, 3)
                )

                if (river < 0.045) {

                    val height = TerrainNoise.terrainHeight(x, z)

                    for (depth in 0..4) {
                        unit.modifier().setBlock(
                            x,
                            height - depth,
                            z,
                            if (depth == 0) Block.WATER else Block.SAND
                        )
                    }
                }
            }
        }
    }
}