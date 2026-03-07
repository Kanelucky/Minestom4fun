package org.kanelucky.world.spawners

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityCreature
import net.minestom.server.instance.Instance
import org.kanelucky.api.java.world.spawner.SpawnRule
import org.kanelucky.api.java.world.spawner.SpawnRuleRegistry

import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Handles natural mob spawning around players
 *
 * @author Kanelucky
 */
object NaturalSpawnManager {

    private const val SPAWN_RADIUS = 48
    private const val MIN_SPAWN_RADIUS = 24
    private const val PASSIVE_MOB_CAP_PER_PLAYER = 10
    private const val SPAWN_INTERVAL_TICKS = 400
    private const val MAX_SPAWN_ATTEMPTS = 12

    private var tickCounter = 0

    fun tick(instance: Instance) {
        tickCounter++
        if (tickCounter < SPAWN_INTERVAL_TICKS) return
        tickCounter = 0

        instance.players.forEach { player ->
            val nearbyPassiveMobs = countPassiveMobsNear(instance, player.position)
            if (nearbyPassiveMobs >= PASSIVE_MOB_CAP_PER_PLAYER) return@forEach
            val slotsLeft = PASSIVE_MOB_CAP_PER_PLAYER - nearbyPassiveMobs
            trySpawnNear(instance, player.position, slotsLeft)
        }
    }

    private fun trySpawnNear(instance: Instance, center: Pos, slotsLeft: Int) {
        repeat(MAX_SPAWN_ATTEMPTS) attempt@{
            val rule = pickRule() ?: return@attempt

            val angle = Random.nextDouble() * 2 * Math.PI
            val dist = Random.nextInt(MIN_SPAWN_RADIUS, SPAWN_RADIUS + 1).toDouble()
            val spawnX = center.x() + dist * cos(angle)
            val spawnZ = center.z() + dist * sin(angle)
            val spawnY = findGroundY(instance, spawnX, center.y(), spawnZ) ?: return@attempt

            val spawnPos = Pos(spawnX, spawnY, spawnZ)
            if (!rule.canSpawn(instance, spawnPos)) return@attempt

            val groupSize = Random.nextInt(rule.minGroupSize, rule.maxGroupSize + 1)
                .coerceAtMost(slotsLeft)
            if (groupSize <= 0) return@attempt

            repeat(groupSize) {
                val offsetX = spawnX + Random.nextDouble(-4.0, 4.0)
                val offsetZ = spawnZ + Random.nextDouble(-4.0, 4.0)
                val offsetY = findGroundY(instance, offsetX, spawnY, offsetZ) ?: return@repeat

                val chunk = instance.getChunkAt(offsetX, offsetZ)
                if (chunk == null || !chunk.isLoaded) return@repeat

                val pos = Pos(offsetX, offsetY, offsetZ)
                if (!rule.canSpawn(instance, pos)) return@repeat

                rule.createEntity().setInstance(instance, pos)
            }
        }
    }

    private fun pickRule(): SpawnRule? {
        val rules = SpawnRuleRegistry.all.filterNotNull()
        if (rules.isEmpty()) {
            return null
        }
        val totalWeight = rules.sumOf { it.weight }
        var random = Random.nextInt(totalWeight)
        for (rule in rules) {
            random -= rule.weight
            if (random < 0) return rule
        }
        return null
    }

    private fun findGroundY(instance: Instance, x: Double, nearY: Double, z: Double): Double? {
        val chunk = instance.getChunkAt(x, z) ?: return null
        if (!chunk.isLoaded) return null
        for (y in 320 downTo -64) {
            try {
                val block = instance.getBlock(x.toInt(), y, z.toInt())
                val above = instance.getBlock(x.toInt(), y + 1, z.toInt())
                if (block.isSolid && !above.isSolid) return y + 1.0
            } catch (e: Exception) {
                return null
            }
        }
        return null
    }

    private fun countPassiveMobsNear(instance: Instance, pos: Pos): Int {
        return instance.getNearbyEntities(pos, 128.0)
            .count { it is EntityCreature }
    }
}