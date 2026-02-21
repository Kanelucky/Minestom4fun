package org.kanelucky.server.world.generator

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator

import org.kanelucky.server.config.ConfigManager
import org.kanelucky.server.world.generator.WorldConstants.AMP
import org.kanelucky.server.world.generator.WorldConstants.BASE_HEIGHT
import org.kanelucky.server.world.generator.WorldConstants.FREQ
import org.kanelucky.server.world.generator.WorldConstants.TREE_CHANCE
import org.kanelucky.server.world.generator.WorldConstants.WATER_LEVEL
import org.kanelucky.server.world.generator.features.decoration.SeagrassDecorator
import org.kanelucky.server.world.generator.noise.FastNoise
import org.kanelucky.server.world.generator.features.`object`.TreeGenerator
import org.kanelucky.server.world.generator.features.decoration.VegetationDecorator
import org.kanelucky.server.world.generator.noise.NoiseConfig
import org.kanelucky.server.world.generator.terrain.CaveCarver
import org.kanelucky.server.world.generator.terrain.RiverCarver

import kotlin.random.Random

class NormalGenerator : Generator {

    private val noise = FastNoise(NoiseConfig.seed)
    private val vegetation = VegetationDecorator()
    private val seagrass = SeagrassDecorator()
    private val caveCarver = CaveCarver()
    private val riverCarver = RiverCarver(noise)

    override fun generate(unit: GenerationUnit) {

        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()

        val baseX = start.blockX()
        val baseZ = start.blockZ()
        val startY = start.blockY()
        val endY = end.blockY()

        val modifier = unit.modifier()

        val heights = Array(16) { IntArray(16) }

        for (x in 0..15) {
            for (z in 0..15) {

                val wx = baseX + x
                val wz = baseZ + z

                var height = BASE_HEIGHT.toDouble()

                for (i in FREQ.indices) {
                    height += noise.noise(wx * FREQ[i], wz * FREQ[i]) * AMP[i]
                }

                heights[x][z] = height.toInt()
            }
        }

        val treePositions = mutableListOf<Pair<Triple<Int,Int,Int>, Block>>()

        for (x in 0..15) {
            for (z in 0..15) {

                val wx = baseX + x
                val wz = baseZ + z
                val height = heights[x][z]

                for (y in startY until endY) {
                    val block = getBlock(y, height, heights, x, z)
                    if (block != null)
                        modifier.setBlock(wx, y, wz, block)
                }

            }
        }

        caveCarver.carve(unit, heights, baseX, baseZ)

        riverCarver.carve(unit, heights, baseX, baseZ)

        seagrass.decorate(unit, heights, baseX, baseZ)

        treePositions.forEach { (pos, groundBlock) ->
            TreeGenerator.tryGenerate(unit, pos.first, pos.second, pos.third, groundBlock)
        }

        vegetation.decorate(unit, heights, baseX, baseZ)

    }

    private fun getBlock(
        y: Int,
        height: Int,
        heights: Array<IntArray>,
        x: Int,
        z: Int
    ): Block? {

        if (y < height - 4) return Block.STONE
        if (y < height - 1) return Block.DIRT

        if (y == height - 1) {

            if (height <= WATER_LEVEL + 1) return Block.SAND

            for (dx in -1..1)
                for (dz in -1..1) {
                    val nx = x + dx
                    val nz = z + dz
                    if (nx in 0..15 && nz in 0..15) {
                        if (heights[nx][nz] < WATER_LEVEL)
                            return Block.SAND
                    }
                }

            return Block.GRASS_BLOCK
        }

        if (y < WATER_LEVEL) {
            if (height < WATER_LEVEL - 2) return Block.WATER
            return Block.SAND
        }

        return null
    }
}