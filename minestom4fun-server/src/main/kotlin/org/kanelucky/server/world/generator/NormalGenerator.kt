package org.kanelucky.server.world.generator

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator

import org.kanelucky.server.config.ConfigManager
import org.kanelucky.server.world.generator.noise.FastNoise

import kotlin.math.abs

/**
 * Normal terrain generator
 *
 * Stable terrain implementation focused on performance,
 * consistency, and long-term world stability
 *
 * Unlike OverworldGenerator, this version does NOT aim
 * to replicate vanilla terrain behavior. Instead, it
 * provides a clean, predictable, and optimized terrain
 *
 * Ported from SwiftMC concepts:
 * https://github.com/XDPXI/SwiftMC
 *
 * Original concept & implementation: XDPXI
 * Stabilized & optimized by: Kanelucky
 */
class NormalGenerator : Generator {

    companion object {
        private const val BASE_HEIGHT = 63
        private const val WATER_LEVEL = 62

        private val FREQ = doubleArrayOf(0.003, 0.01, 0.05)
        private val AMP  = doubleArrayOf(35.0, 12.0, 4.0)

        private const val TREE_CHANCE = 0.0025

        private const val VEGETATION_CHANCE = 0.22
        private const val FLOWER_CHANCE = 0.04
        private const val TALL_GRASS_CHANCE = 0.25

        private const val SEAGRASS_CHANCE = 0.18
        private const val TALL_SEAGRASS_CHANCE = 0.35
        private const val MIN_WATER_DEPTH = 3
    }

    private val noise = FastNoise(ConfigManager.serverSettings.seed)

    override fun generate(unit: GenerationUnit) {

        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()

        val baseX = start.blockX()
        val baseZ = start.blockZ()

        val startY = start.blockY()
        val endY = end.blockY()

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

        val trees = mutableListOf<Triple<Int,Int,Int>>()
        val vegetation = mutableListOf<Triple<Int,Int,Int>>()

        for (x in 0..15) {
            for (z in 0..15) {

                val wx = baseX + x
                val wz = baseZ + z
                val height = heights[x][z]

                for (y in startY until endY) {

                    val block = getBlock(y, height, heights, x, z)
                    if (block != null)
                        unit.modifier().setBlock(wx, y, wz, block)
                }
                placeSeagrass(unit, wx, heights[x][z], wz)

                if (height > WATER_LEVEL &&
                    height < WATER_LEVEL + 20 &&
                    Math.random() < TREE_CHANCE &&
                    x in 2..13 && z in 2..13
                ) {
                    trees += Triple(wx, height, wz)
                }

                if (height > WATER_LEVEL &&
                    x in 1..14 && z in 1..14 &&
                    heights[x][z] > WATER_LEVEL + 1
                ) {
                    if (Math.random() < VEGETATION_CHANCE) {
                        vegetation += Triple(wx, height, wz)
                    }
                }
            }
        }

        trees.forEach { placeTree(unit, it.first, it.second, it.third) }
        vegetation.forEach { placeVegetation(unit, it.first, it.second, it.third) }
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

    private fun placeTree(unit: GenerationUnit, x: Int, y: Int, z: Int) {
        val trunkHeight = (4..6).random()

        for (i in 0..trunkHeight)
            unit.modifier().setBlock(x, y + i, z, Block.OAK_LOG)

        val top = y + trunkHeight

        for (dx in -2..2)
            for (dz in -2..2)
                for (dy in -2..2) {

                    val dist = abs(dx) + abs(dz) + abs(dy)

                    if (dist < 4) {
                        unit.modifier().setBlock(
                            x + dx,
                            top + dy,
                            z + dz,
                            Block.OAK_LEAVES
                        )
                    }
                }
    }

    private fun placeVegetation(unit: GenerationUnit, x: Int, y: Int, z: Int) {

        val roll = Math.random()

        if (roll < FLOWER_CHANCE) {
            val flower = when ((0..4).random()) {
                0 -> Block.POPPY
                1 -> Block.DANDELION
                2 -> Block.AZURE_BLUET
                3 -> Block.OXEYE_DAISY
                else -> Block.CORNFLOWER
            }

            unit.modifier().setBlock(x, y, z, flower)
            return
        }

        if (roll < TALL_GRASS_CHANCE) {
            unit.modifier().setBlock(x, y, z, Block.TALL_GRASS.withProperty("half", "lower"))
            unit.modifier().setBlock(x, y + 1, z, Block.TALL_GRASS.withProperty("half", "upper"))
            return
        }

        unit.modifier().setBlock(x, y, z, Block.SHORT_GRASS)
    }

    private fun placeSeagrass(unit: GenerationUnit, x: Int, terrainHeight: Int, z: Int) {

        val depth = WATER_LEVEL - terrainHeight

        if (depth < MIN_WATER_DEPTH) return

        val floorY = terrainHeight

        val plantY = floorY

        if (depth <= 3) return

        if (Math.random() < SEAGRASS_CHANCE) {
            unit.modifier().setBlock(x, plantY, z, Block.SEAGRASS)
        }

        if (depth >= 5 && Math.random() < TALL_SEAGRASS_CHANCE) {
            unit.modifier().setBlock(x, plantY, z, Block.TALL_SEAGRASS)
        }

        if (depth > 6 && Math.random() < 0.08) {
            val height = (2..5).random()
            for (i in 0 until height) {
                unit.modifier().setBlock(x, plantY + i, z, Block.KELP_PLANT)
            }
        }
    }
}