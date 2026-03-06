package org.kanelucky.entity.passive

import net.minestom.server.entity.EntityType
import net.minestom.server.item.Material
import net.minestom.server.sound.SoundEvent

/**
 * @author Kanelucky
 */
class EntityPig : PassiveMob(EntityType.PIG) {

    override val breedingItems = setOf(
        Material.CARROT,
        Material.POTATO,
        Material.BEETROOT
    )

    override fun createOffspring(): AnimalEntity = EntityPig()

    override fun getHurtSound() = SoundEvent.ENTITY_PIG_HURT
}