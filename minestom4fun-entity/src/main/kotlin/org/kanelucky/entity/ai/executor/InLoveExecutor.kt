package org.kanelucky.entity.ai.executor

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.EntityCreature
import net.minestom.server.network.packet.server.play.ParticlePacket
import net.minestom.server.particle.Particle
import org.kanelucky.api.java.entity.ai.memory.MemoryTypes

import org.kanelucky.entity.IntelligentEntity

import kotlin.random.Random

/**
 * Sets the entity in love mode for a duration and shows heart particles.
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class InLoveExecutor(private val duration: Int = 600) : BehaviorExecutor {

    private var tickCounter = 0

    override fun onStart(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        tickCounter = 0
        entity.behaviorGroup.memoryStorage.set(MemoryTypes.IS_IN_LOVE, true)
        entity.behaviorGroup.memoryStorage.set(MemoryTypes.LAST_IN_LOVE_TIME, entity.aliveTicks)
    }

    override fun execute(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false
        tickCounter++

        if (tickCounter > duration ||
            entity.behaviorGroup.memoryStorage.get(MemoryTypes.IS_IN_LOVE) != true) {
            return false
        }

        if (tickCounter % 10 == 0) {
            spawnHeartParticle(entity)
        }
        return true
    }

    override fun onStop(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        entity.behaviorGroup.memoryStorage.set(MemoryTypes.IS_IN_LOVE, false)
    }

    override fun onInterrupt(entity: EntityCreature) = onStop(entity)

    private fun spawnHeartParticle(entity: EntityCreature) {
        val pos = entity.position
        val w = entity.boundingBox.width() / 2.0
        val packet = ParticlePacket(
            Particle.HEART,
            Pos(
                pos.x() + Random.nextDouble(-w, w),
                pos.y() + entity.eyeHeight + 0.3,
                pos.z() + Random.nextDouble(-w, w)
            ),
            Vec(0.0, 0.0, 0.0),
            0f,
            1
        )
        entity.sendPacketToViewers(packet)
    }
}