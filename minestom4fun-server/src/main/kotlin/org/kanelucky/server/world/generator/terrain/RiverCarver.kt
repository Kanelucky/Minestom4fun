package org.kanelucky.server.world.generator.terrain

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import org.kanelucky.server.world.generator.WorldConstants.CAVE_MIN_Y
import org.kanelucky.server.world.generator.WorldConstants.CAVE_SURFACE_BUFFER
import org.kanelucky.server.world.generator.WorldConstants.CAVE_THRESHOLD
import org.kanelucky.server.world.generator.WorldConstants.MIN_WATER_DEPTH
import org.kanelucky.server.world.generator.WorldConstants.RIVER_DEPTH
import org.kanelucky.server.world.generator.WorldConstants.RIVER_THRESHOLD
import org.kanelucky.server.world.generator.WorldConstants.SEAGRASS_CHANCE
import org.kanelucky.server.world.generator.WorldConstants.TUNNEL_THRESHOLD
import org.kanelucky.server.world.generator.WorldConstants.WATER_LEVEL
import org.kanelucky.server.world.generator.noise.FastNoise
import org.kanelucky.server.world.generator.noise.NoiseConfig.CAVE_SCALE_MAIN
import org.kanelucky.server.world.generator.noise.NoiseConfig.CAVE_SCALE_TUNNEL

import kotlin.random.Random

/**
 * @author Kanelucky
 */
class RiverCarver(private val noise: FastNoise) {

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
                val surface = heights[lx][lz]

                val river = kotlin.math.abs(noise.fractal2D(wx * 0.004, wz * 0.004, 3))

                if (river < RIVER_THRESHOLD && surface > WATER_LEVEL + 3) {
                    val riverBottom = surface - RIVER_DEPTH

                    for (y in riverBottom..surface) {
                        modifier.setBlock(wx, y, wz, Block.AIR)
                    }

                    modifier.setBlock(wx, riverBottom - 1, wz, Block.SAND)

                    for (y in riverBottom until surface) {
                        modifier.setBlock(wx, y, wz, Block.WATER)
                    }

                    heights[lx][lz] = WATER_LEVEL - 1

                    val riverDepth = surface - riverBottom
                    if (riverDepth >= MIN_WATER_DEPTH && Random.nextFloat() < SEAGRASS_CHANCE) {
                        modifier.setBlock(wx, riverBottom, wz, Block.SEAGRASS)
                    }
                }

                val maxY = surface - CAVE_SURFACE_BUFFER
                if (maxY <= CAVE_MIN_Y) continue

                for (y in CAVE_MIN_Y until maxY) {

                    val main = noise.noise(
                        wx * CAVE_SCALE_MAIN,
                        y * CAVE_SCALE_MAIN,
                        wz * CAVE_SCALE_MAIN
                    )

                    val tunnel = noise.noise(
                        wx * CAVE_SCALE_TUNNEL,
                        y * 0.02,
                        wz * CAVE_SCALE_TUNNEL
                    )

                    if (main > CAVE_THRESHOLD || tunnel > TUNNEL_THRESHOLD) {
                        modifier.setBlock(wx, y, wz, Block.AIR)
                    }
                }
            }
        }
    }
}