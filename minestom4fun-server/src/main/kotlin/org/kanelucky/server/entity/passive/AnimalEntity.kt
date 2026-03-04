package org.kanelucky.server.entity.passive

import net.kyori.adventure.sound.Sound

import net.minestom.server.entity.EntityType
import net.minestom.server.entity.damage.Damage
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.sound.SoundEvent

import org.kanelucky.server.entity.IntelligentEntity
import org.kanelucky.server.entity.ai.memory.MemoryTypes

/**
 * Base class for breedable animals.
 *
 * @author Kanelucky
 */
abstract class AnimalEntity(entityType: EntityType) : IntelligentEntity(entityType) {

    var isBaby: Boolean = false
    var ageTicks: Int = 0
    var breedCooldown: Int = 0

    abstract val breedingItems: Set<Material>
    abstract fun createOffspring(): AnimalEntity

    fun isBreedingItem(item: ItemStack) = item.material() in breedingItems

    override fun damage(damage: Damage): Boolean {
        val result = super.damage(damage)
        if (result) {
            instance?.playSound(
                Sound.sound(getHurtSound(), Sound.Source.NEUTRAL, 1f, 1f),
                position
            )
            behaviorGroup.memoryStorage.set(MemoryTypes.PANIC_TICKS, 100)
        }
        return result
    }

    abstract fun getHurtSound(): SoundEvent

    override fun update(time: Long) {
        super.update(time)
        if (isBaby) {
            ageTicks++
            if (ageTicks >= 24000) {
                isBaby = false
                ageTicks = 0
            }
        }
        if (breedCooldown > 0) breedCooldown--
    }
}