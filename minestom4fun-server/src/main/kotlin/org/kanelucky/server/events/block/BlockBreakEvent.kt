package org.kanelucky.server.events.block

import net.minestom.server.entity.GameMode
import net.minestom.server.entity.ItemEntity
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.item.ItemStack

import java.time.Duration

/**
 * @author Kanelucky
 */
object BlockBreakEvent {

    @JvmStatic
    fun node(): EventNode<Event> {
        val node = EventNode.all("block-drop-on-break")

        node.addListener(PlayerBlockBreakEvent::class.java) { event ->

            val player = event.player

            if (player.gameMode == GameMode.CREATIVE) return@addListener

            event.block.registry()?.material()?.let { material ->

                val itemEntity = ItemEntity(ItemStack.of(material))

                itemEntity.setPickupDelay(Duration.ofMillis(500))

                itemEntity.setInstance(
                    event.instance,
                    event.blockPosition.add(0.5, 0.5, 0.5)
                )

            }
        }

        return node
    }
}