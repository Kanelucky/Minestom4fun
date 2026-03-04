package org.kanelucky.server.world.spawners

import net.minestom.server.coordinate.Pos
import net.minestom.server.instance.Instance

import kotlin.random.Random

/**
 * Handles natural mob spawning around players
 *
 * @author Kanelucky
 */
object NaturalSpawnManager {

    private const val SPAWN_RADIUS = 48
    private const val MIN_SPAWN_RADIUS = 24
    private const val MAX_ENTITIES_NEARBY = 10
    private const val SPAWN_INTERVAL_TICKS = 200

    private var tickCounter = 0

    fun tick(instance: Instance) {
        tickCounter++
        if (tickCounter < SPAWN_INTERVAL_TICKS) return
        tickCounter = 0

        instance.players.forEach { player ->
            trySpawnNear(instance, player.position)
        }
    }

    private fun trySpawnNear(instance: Instance, center: Pos) {
        val rule = pickRule() ?: return

        val angle = Random.nextDouble() * 2 * Math.PI
        val dist = Random.nextInt(MIN_SPAWN_RADIUS, SPAWN_RADIUS + 1).toDouble()
        val spawnX = center.x() + dist * Math.cos(angle)
        val spawnZ = center.z() + dist * Math.sin(angle)
        val spawnY = findGroundY(instance, spawnX, center.y(), spawnZ) ?: return

        val spawnPos = Pos(spawnX, spawnY, spawnZ)
        if (!rule.canSpawn(instance, spawnPos)) return
        if (countNearbyEntities(instance, center) >= MAX_ENTITIES_NEARBY) return

        val groupSize = Random.nextInt(rule.minGroupSize, rule.maxGroupSize + 1)
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

    private fun pickRule(): SpawnRule? {
        val rules = SpawnRules.ALL
        if (rules.isEmpty()) return null
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

    private fun countNearbyEntities(instance: Instance, pos: Pos): Int {
        return instance.entities.count { entity ->
            entity.position.distanceSquared(pos) <= 128 * 128
        }
    }
}