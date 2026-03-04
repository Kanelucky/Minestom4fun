package org.kanelucky.server.entity.passive

import net.minestom.server.entity.EntityType
import net.minestom.server.item.Material
import net.minestom.server.sound.SoundEvent

/**
 * @author Kanelucky
 */
class EntityChicken : PassiveMob(EntityType.CHICKEN) {

    override val breedingItems = setOf(
        Material.WHEAT_SEEDS,
        Material.MELON_SEEDS,
        Material.PUMPKIN_SEEDS,
        Material.BEETROOT_SEEDS
    )

    override fun createOffspring(): AnimalEntity = EntityChicken()

    override fun getHurtSound() = SoundEvent.ENTITY_CHICKEN_HURT
}