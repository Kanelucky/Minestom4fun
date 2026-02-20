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
 * Key goals:
 * - deterministic & stable generation
 * - fast performance under heavy load
 * - smooth terrain shaping
 * - minimal generation artifacts
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

                if (height > WATER_LEVEL &&
                    height < WATER_LEVEL + 20 &&
                    Math.random() < TREE_CHANCE &&
                    x in 2..13 && z in 2..13
                ) {
                    trees += Triple(wx, height, wz)
                }
            }
        }

        trees.forEach { placeTree(unit, it.first, it.second, it.third) }
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
}