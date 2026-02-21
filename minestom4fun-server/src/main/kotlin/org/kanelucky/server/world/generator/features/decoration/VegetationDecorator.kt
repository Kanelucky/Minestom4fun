package org.kanelucky.server.world.generator.features.decoration

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import org.kanelucky.server.world.generator.WorldConstants
import org.kanelucky.server.world.generator.features.objects.TreeGenerator

import kotlin.random.Random

/**
 * @author Kanelucky
 */
class VegetationDecorator {

    companion object {
        private const val TREE_CHANCE = 0.01f
        private const val FLOWER_CHANCE = 0.03f
        private const val TALL_GRASS_CHANCE = 0.08f
        private const val VEGETATION_DENSITY = 0.75f

        private val VALID_GROUND = setOf(
            Block.GRASS_BLOCK,
            Block.DIRT,
            Block.COARSE_DIRT,
            Block.ROOTED_DIRT,
            Block.PODZOL
        )
    }

    fun decorate(
        unit: GenerationUnit,
        heights: Array<IntArray>,
        baseX: Int,
        baseZ: Int
    ) {
        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()
        val modifier = unit.modifier()

        for (lx in 0 until 16) {
            for (lz in 0 until 16) {

                val x = baseX + lx
                val z = baseZ + lz
                val height = heights[lx][lz]
                val surfaceY = height - 1

                if (surfaceY <= start.blockY() || surfaceY + 1 >= end.blockY())
                    continue

                if (height <= WorldConstants.WATER_LEVEL + 1) continue

                var isSand = false
                outer@ for (dx in -1..1)
                    for (dz in -1..1) {
                        val nx = lx + dx
                        val nz = lz + dz
                        if (nx in 0..15 && nz in 0..15) {
                            if (heights[nx][nz] < WorldConstants.WATER_LEVEL) {
                                isSand = true
                                break@outer
                            }
                        }
                    }

                if (isSand) continue

                if (Random.Default.nextFloat() > VEGETATION_DENSITY) continue

                val vegetationY = surfaceY + 1

                if (Random.Default.nextFloat() < TREE_CHANCE) {
                    TreeGenerator.tryGenerate(unit, x, vegetationY, z, Block.GRASS_BLOCK)
                    continue
                }

                val roll = Random.Default.nextFloat()
                when {
                    roll < FLOWER_CHANCE     -> modifier.setBlock(x, vegetationY, z, randomFlower())
                    roll < TALL_GRASS_CHANCE -> {
                        modifier.setBlock(x, vegetationY, z, Block.TALL_GRASS.withProperty("half", "lower"))
                        modifier.setBlock(x, vegetationY + 1, z, Block.TALL_GRASS.withProperty("half", "upper"))
                    }
                    else -> modifier.setBlock(x, vegetationY, z, Block.SHORT_GRASS)
                }
            }
        }
    }

    private fun randomFlower() = when ((0..4).random()) {
        0 -> Block.POPPY
        1 -> Block.DANDELION
        2 -> Block.AZURE_BLUET
        3 -> Block.OXEYE_DAISY
        else -> Block.CORNFLOWER
    }
}