package org.kanelucky.server.entity.ai.executor

import net.minestom.server.entity.EntityCreature
import net.minestom.server.instance.block.Block

import kotlin.math.floor

/**
 * Sheep-specific grass eating executor. Plays the eat animation,
 * then destroys short grass or converts grass block to dirt
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
open class EatGrassExecutor(private val duration: Int = 40) : BehaviorExecutor {

    private var tickCounter = 0

    override fun onStart(entity: EntityCreature) {
        tickCounter = 0
    }

    override fun execute(entity: EntityCreature): Boolean {
        tickCounter++
        return tickCounter < duration
    }

    override fun onStop(entity: EntityCreature) {
        val instance = entity.instance ?: return
        val pos = entity.position
        val x = floor(pos.x()).toInt()
        val y = floor(pos.y()).toInt()
        val z = floor(pos.z()).toInt()

        val feetBlock = instance.getBlock(x, y, z)
        if (feetBlock == Block.SHORT_GRASS) {
            instance.setBlock(x, y, z, Block.AIR)
            onEatGrass(entity)
            return
        }

        val belowBlock = instance.getBlock(x, y - 1, z)
        if (belowBlock == Block.GRASS_BLOCK) {
            instance.setBlock(x, y - 1, z, Block.DIRT)
            onEatGrass(entity)
        }
    }

    protected open fun onEatGrass(entity: EntityCreature) {}
}