package org.kanelucky.event.player

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

            val itemEntity = ItemEntity(event.itemStack)
            itemEntity.setInstance(event.instance, event.player.position)
            itemEntity.setVelocity(
                event.player.position
                    .add(0.0, 1.0, 0.0)
                    .direction()
                    .mul(6.0)
            )
            itemEntity.setPickupDelay(Duration.ofMillis(500))
        }
        return playerNode
    }
}