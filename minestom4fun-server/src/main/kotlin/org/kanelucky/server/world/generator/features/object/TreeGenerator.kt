package org.kanelucky.server.world.generator.features.`object`

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import kotlin.math.abs

/**
 * @author Kanelucky
 */
object TreeGenerator {

    private val VALID_GROUND = setOf(
        Block.GRASS_BLOCK,
        Block.DIRT,
        Block.COARSE_DIRT,
        Block.ROOTED_DIRT,
        Block.PODZOL
    )

    fun tryGenerate(
        unit: GenerationUnit,
        x: Int,
        y: Int,
        z: Int,
        groundBlock: Block
    ) {
        if (!canPlace(unit, x, z, groundBlock)) return
        generate(unit, x, y, z)
    }

    private fun generate(
        unit: GenerationUnit,
        x: Int,
        y: Int,
        z: Int
    ) {
        if (Math.random() < 0.1) generateFancy(unit, x, y, z)
        else generateSmall(unit, x, y, z)
    }

    private fun generateSmall(
        unit: GenerationUnit,
        x: Int,
        y: Int,
        z: Int
    ) {
        val modifier = unit.modifier()
        val trunkHeight = (4..6).random()

        for (i in 0 until trunkHeight)
            modifier.setBlock(x, y + i, z, Block.OAK_LOG)

        val top = y + trunkHeight - 1

        for (dy in -1..0)
            for (dx in -2..2)
                for (dz in -2..2) {
                    if (abs(dx) == 2 && abs(dz) == 2) continue
                    safeSet(unit, x + dx, top + dy, z + dz, Block.OAK_LEAVES)
                }

        for (dx in -1..1)
            for (dz in -1..1)
                safeSet(unit, x + dx, top + 1, z + dz, Block.OAK_LEAVES)

        for ((dx, dz) in listOf(0 to 0, 1 to 0, -1 to 0, 0 to 1, 0 to -1))
            safeSet(unit, x + dx, top + 2, z + dz, Block.OAK_LEAVES)
    }

    private fun generateFancy(
        unit: GenerationUnit,
        x: Int,
        y: Int,
        z: Int
    ) {
        val modifier = unit.modifier()

        val heightLimit = (5..16).random()
        val trunkHeight = (heightLimit * 0.618).toInt()
        val leafDistanceLimit = 4
        val branchSlope = 0.381
        val sphereRadius = heightLimit * 0.664
        val branchCount = if (heightLimit >= 11) 2 else 1

        for (i in 0..trunkHeight)
            modifier.setBlock(x, y + i, z, Block.OAK_LOG)

        placeLeaveSphere(unit, x, y + heightLimit, z, leafDistanceLimit)

        val branchStartY = Math.ceil(heightLimit * 0.3).toInt()
        val branchEndY = heightLimit - leafDistanceLimit

        for (branchY in branchStartY..branchEndY) {
            val actualY = y + branchY

            val distFromCenter = abs(branchY - heightLimit / 2.0)
            val maxHDist = maxOf(0.0, Math.sqrt(sphereRadius * sphereRadius - distFromCenter * distFromCenter) - 1.5)

            if (maxHDist <= 0) continue

            repeat(branchCount) {

                val minDist = maxHDist * 0.247
                val branchDist = minDist + Math.random() * (maxHDist - minDist)
                val angle = Math.random() * 2 * Math.PI

                val endX = x + (branchDist * Math.cos(angle)).toInt()
                val endZ = z + (branchDist * Math.sin(angle)).toInt()

                val connectY = actualY - (branchDist * branchSlope).toInt()

                placeLeaveSphere(unit, endX, actualY, endZ, leafDistanceLimit)

                if (connectY > y + heightLimit * 0.2) {
                    val dx = endX - x
                    val dz = endZ - z
                    val steps = maxOf(abs(dx), abs(dz))

                    if (steps > 0) {
                        for (step in 0..steps) {
                            val t = step.toDouble() / steps
                            val lx = (x + dx * t).toInt()
                            val lz = (z + dz * t).toInt()
                            val ly = (connectY + (actualY - connectY) * t).toInt()
                            safeSet(unit, lx, ly, lz, Block.OAK_LOG)
                        }
                    }
                }
            }
        }
    }

    private fun placeLeaveSphere(
        unit: GenerationUnit,
        cx: Int,
        cy: Int,
        cz: Int,
        radius: Int
    ) {
        for (dx in -radius..radius)
            for (dz in -radius..radius)
                for (dy in -radius..radius)
                    if (dx * dx + dy * dy + dz * dz <= radius * radius)
                        safeSet(unit, cx + dx, cy + dy, cz + dz, Block.OAK_LEAVES)
    }

    private fun canPlace(
        unit: GenerationUnit,
        x: Int,
        z: Int,
        groundBlock: Block
    ): Boolean {
        val start = unit.absoluteStart()
        val end = unit.absoluteEnd()

        val withinBounds = x > start.blockX() + 2 &&
                x < end.blockX() - 3 &&
                z > start.blockZ() + 2 &&
                z < end.blockZ() - 3

        if (!withinBounds) return false

        if (groundBlock == Block.WATER ||
            groundBlock == Block.SAND ||
            groundBlock == Block.AIR
        ) return false

        return groundBlock in VALID_GROUND

    }

    private fun safeSet(
        unit: GenerationUnit,
        x: Int,
        y: Int,
        z: Int,
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