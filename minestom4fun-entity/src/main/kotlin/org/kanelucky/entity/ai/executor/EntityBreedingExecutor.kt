package org.kanelucky.entity.ai.executor

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityCreature
import org.kanelucky.api.java.entity.ai.memory.MemoryTypes

import org.kanelucky.entity.IntelligentEntity
import org.kanelucky.entity.ai.memory.EntityMemoryTypes.ENTITY_SPOUSE
import org.kanelucky.entity.passive.AnimalEntity

/**
 * Finds a nearby in-love entity of the same type, moves toward it, and spawns a baby
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class EntityBreedingExecutor(
    private val duration: Int = 60,
    private val speed: Double = 0.3
) : BehaviorExecutor {

    companion object {
        private const val FIND_RANGE_SQ = 256.0
        private const val BREED_DIST_SQ = 4.0
    }

    private var tickCounter = 0
    private var spouse: AnimalEntity? = null
    private var isInitiator = false
    private var lastSpousePos: Pos? = null

    override fun onStart(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        tickCounter = 0
        spouse = null
        isInitiator = false
        lastSpousePos = null
        entity.getAttribute(net.minestom.server.entity.attribute.Attribute.MOVEMENT_SPEED).baseValue = speed
    }

    override fun execute(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false
        if (entity !is AnimalEntity) return false
        tickCounter++

        // Resolve spouse
        if (spouse == null || spouse!!.isDead) {
            spouse = (entity.behaviorGroup.memoryStorage.get(ENTITY_SPOUSE) as? AnimalEntity)
                ?.takeIf { !it.isDead }
                ?: findSpouse(entity).also { found ->
                    if (found != null) {
                        isInitiator = true
                        entity.behaviorGroup.memoryStorage.set(ENTITY_SPOUSE, found)
                        (found as? IntelligentEntity)?.behaviorGroup?.memoryStorage?.set(ENTITY_SPOUSE, entity)
                    }
                }
        }

        val currentSpouse = spouse ?: return tickCounter < duration + 60

        // Move toward spouse
        val spousePos = currentSpouse.position
        val target = Pos(spousePos.x(), spousePos.y(), spousePos.z())
        if (lastSpousePos == null || lastSpousePos!!.distanceSquared(target) > 1.0) {
            EntityControlHelper.setRouteTarget(entity, target)
            lastSpousePos = target
        }
        EntityControlHelper.setLookTarget(entity,
            Pos(spousePos.x(), spousePos.y() + currentSpouse.eyeHeight, spousePos.z())
        )

        // Only initiator spawns baby
        if (isInitiator) {
            val distSq = entity.position.distanceSquared(spousePos)
            if (distSq < BREED_DIST_SQ && tickCounter >= duration) {
                spawnBaby(entity, currentSpouse)
                clearLoveState(entity)
                clearLoveState(currentSpouse)
                return false
            }
        }

        return tickCounter < duration + 60
    }

    override fun onStop(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        entity.getAttribute(net.minestom.server.entity.attribute.Attribute.MOVEMENT_SPEED).baseValue = 0.23
        EntityControlHelper.removeRouteTarget(entity)
        EntityControlHelper.removeLookTarget(entity)
        entity.behaviorGroup.memoryStorage.clear(ENTITY_SPOUSE)
        spouse = null
        isInitiator = false
    }

    override fun onInterrupt(entity: EntityCreature) = onStop(entity)

    private fun findSpouse(entity: AnimalEntity): AnimalEntity? {
        return entity.instance?.entities
            ?.filterIsInstance<AnimalEntity>()
            ?.filter { candidate ->
                candidate !== entity &&
                        candidate.entityType == entity.entityType &&
                        !candidate.isBaby &&
                        !candidate.isDead &&
                        candidate.breedCooldown <= 0 &&
                        candidate is IntelligentEntity &&
                        candidate.behaviorGroup.memoryStorage.get(MemoryTypes.IS_IN_LOVE) == true &&
                        candidate.behaviorGroup.memoryStorage.get(ENTITY_SPOUSE) == null &&
                        entity.position.distanceSquared(candidate.position) <= FIND_RANGE_SQ
            }
            ?.minByOrNull { entity.position.distanceSquared(it.position) }
    }

    private fun spawnBaby(parent: AnimalEntity, spouse: AnimalEntity) {
        val baby = parent.createOffspring()
        baby.isBaby = true
        // Prevent baby from immediately breeding
        baby.breedCooldown = 24000
        baby.setInstance(parent.instance!!, parent.position)

        // Breeding cooldown for parents
        parent.breedCooldown = 6000
        spouse.breedCooldown = 6000
    }

    private fun clearLoveState(entity: AnimalEntity) {
        if (entity is IntelligentEntity) {
            entity.behaviorGroup.memoryStorage.set(MemoryTypes.IS_IN_LOVE, false)
            entity.behaviorGroup.memoryStorage.clear(ENTITY_SPOUSE)
        }
    }
}