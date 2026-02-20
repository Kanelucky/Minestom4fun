package org.kanelucky.server.events.player

import net.minestom.server.entity.ItemEntity
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.trait.PlayerEvent

import java.time.Duration

/**
 * @author Kanelucky
 */
object PlayerDropItemEvent {
    @JvmStatic
    fun node(): EventNode<PlayerEvent> {
        val playerNode =
            EventNode.type("player-drop-item", EventFilter.PLAYER)

        playerNode.addListener(ItemDropEvent::class.java) { event ->
            val player = event.player

            val playerPos = player.position
            val direction = playerPos.direction()

            val spawnPos = playerPos
                .add(0.0, player.eyeHeight.toDouble(), 0.0)
                .add(direction.mul(0.3))

            val velocity = direction.mul(4.0)

            val itemEntity = ItemEntity(event.itemStack)

            itemEntity.setInstance(event.instance, spawnPos)
            itemEntity.setVelocity(velocity)
            itemEntity.setPickupDelay(Duration.ofMillis(500))
        }

        return playerNode
    }
}