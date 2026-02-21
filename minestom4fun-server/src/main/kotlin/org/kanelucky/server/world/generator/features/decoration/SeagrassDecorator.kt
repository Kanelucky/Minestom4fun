package org.kanelucky.server.world.generator.features.decoration

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import org.kanelucky.server.world.generator.WorldConstants.MIN_WATER_DEPTH
import org.kanelucky.server.world.generator.WorldConstants.SEAGRASS_CHANCE
import org.kanelucky.server.world.generator.WorldConstants.TALL_SEAGRASS_CHANCE
import org.kanelucky.server.world.generator.WorldConstants.WATER_LEVEL

import kotlin.random.Random

/**
 * @author Kanelucky
 */
class SeagrassDecorator {

    fun decorate(
        unit: GenerationUnit,
        heights: Array<IntArray>,
        baseX: Int,
        baseZ: Int
    ) {
        for (x in 0..15)
            for (z in 0..15)
                placeSeagrass(unit, baseX + x, heights[x][z], baseZ + z)
    }

    private fun placeSeagrass(
        unit: GenerationUnit,
        x: Int,
        terrainHeight: Int,
        z: Int
    ) {

        val depth = WATER_LEVEL - terrainHeight
        if (depth < MIN_WATER_DEPTH) return

        val floorY = terrainHeight

        if (Random.nextFloat() < SEAGRASS_CHANCE) {
            unit.modifier().setBlock(x, floorY, z, Block.SEAGRASS)
        }

        if (depth >= 5 && Random.nextFloat() < TALL_SEAGRASS_CHANCE) {
            unit.modifier().setBlock(x, floorY, z, Block.TALL_SEAGRASS)
        }

        if (depth > 6 && Random.nextFloat() < 0.08f) {
            val height = Random.nextInt(2, 6)
            for (i in 0 until height) {
                unit.modifier().setBlock(x, floorY + i, z, Block.KELP_PLANT)
            }
        }
    }
}