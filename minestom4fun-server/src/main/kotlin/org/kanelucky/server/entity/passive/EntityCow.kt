package org.kanelucky.server.entity.passive;

import net.minestom.server.entity.EntityType
import net.minestom.server.item.Material
import net.minestom.server.sound.SoundEvent

class EntityCow : PassiveMob(EntityType.COW) {

    override val breedingItems = setOf(Material.WHEAT)
    var isSheared: Boolean = false

    override fun createOffspring(): AnimalEntity = EntityCow()

    override fun getHurtSound() = SoundEvent.ENTITY_COW_HURT
}