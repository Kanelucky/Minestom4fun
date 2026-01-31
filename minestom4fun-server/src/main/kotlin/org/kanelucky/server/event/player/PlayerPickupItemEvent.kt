package org.kanelucky.server.event.player

import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.item.PickupItemEvent

import java.util.function.Consumer

/**
 * @author Kanelucky
 */
object PlayerPickupItemEvent {
    @JvmStatic
    fun node(): EventNode<Event> {

        val node = EventNode.all("player-pick-up-item")

        node.addListener<PickupItemEvent>(PickupItemEvent::class.java, Consumer { event: PickupItemEvent? ->
            val itemStack = event!!.getItemStack()
            if (event.livingEntity is Player) {

                val player = event.livingEntity as Player

                player.inventory.addItemStack(itemStack)

            }
        })

        return node
    }
}
