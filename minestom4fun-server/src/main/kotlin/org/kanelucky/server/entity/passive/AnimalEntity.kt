package org.kanelucky.server.entity.passive

import net.minestom.server.entity.EntityType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import org.kanelucky.server.entity.IntelligentEntity

/**
 * Base class for breedable animals.
 *
 * @author Kanelucky
 */
abstract class AnimalEntity(entityType: EntityType) : IntelligentEntity(entityType) {

    var isBaby: Boolean = false
        set(value) {
            field = value
            if (value) {
                setBoundingBox(boundingBox.expand(-0.5, -0.5, -0.5))
            } else {
                setBoundingBox(boundingBox)
            }
        }

    var ageTicks: Int = 0

    var breedCooldown: Int = 0

    abstract val breedingItems: Set<Material>

    fun isBreedingItem(item: ItemStack): Boolean {
        return item.material() in breedingItems
    }

    override fun update(time: Long) {
        super.update(time)

        if (isBaby) {
            ageTicks++
            if (ageTicks >= 24000) {
                isBaby = false
                ageTicks = 0
            }
        }

        if (breedCooldown > 0) {
            breedCooldown--
        }
    }
}