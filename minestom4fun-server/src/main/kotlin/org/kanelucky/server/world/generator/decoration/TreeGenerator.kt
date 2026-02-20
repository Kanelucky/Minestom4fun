package org.kanelucky.server.world.generator.decoration

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import kotlin.math.abs
import kotlin.random.Random

/**
 * @author Kanelucky
 */
object TreeGenerator {

    fun generate(unit: GenerationUnit, x: Int, y: Int, z: Int) {

        if (!canPlace(unit, x, z)) return

        val modifier = unit.modifier()
        val height = Random.nextInt(4, 7)


        for (i in 0 until height) {
            modifier.setBlock(x, y + i, z, Block.OAK_LOG)
        }

        for (ox in -2..2)
            for (oz in -2..2)
                for (oy in height - 2..height)
                    if (abs(ox) + abs(oz) < 4) {
                        safeSet(unit, x + ox, y + oy, z + oz, Block.OAK_LEAVES)
                    }
    }


    private fun canPlace(unit: GenerationUnit, x: Int, z: Int): Boolean {
        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()

        return !(x <= start.blockX() + 2 ||
                x >= end.blockX() - 3 ||
                z <= start.blockZ() + 2 ||
                z >= end.blockZ() - 3)
    }

    private fun safeSet(
        unit: GenerationUnit,
        x: Int, y: Int, z: Int,
        block: Block
    ) {
        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()

        if (x in start.blockX() until end.blockX() &&
            y in start.blockY() until end.blockY() &&
            z in start.blockZ() until end.blockZ()
        ) {
            unit.modifier().setBlock(x, y, z, block)
        }
    }
}