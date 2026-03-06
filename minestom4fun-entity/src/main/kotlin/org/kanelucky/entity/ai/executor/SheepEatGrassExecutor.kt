package org.kanelucky.entity.ai.executor

import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.metadata.animal.SheepMeta

import org.kanelucky.entity.passive.EntitySheep

/**
 * Extended grass eating executor for sheep that regrows wool after eating
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class SheepEatGrassExecutor : EatGrassExecutor() {

    override fun onEatGrass(entity: EntityCreature) {
        if (entity is EntitySheep) {
            val meta = entity.entityMeta as SheepMeta

            if (meta.isSheared) {
                meta.isSheared = false
            }
        }
    }
}