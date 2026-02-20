package org.kanelucky.server.events

import net.minestom.server.event.GlobalEventHandler

import org.kanelucky.server.events.block.BlockBreakEvent
import org.kanelucky.server.events.player.PlayerDeathEvent
import org.kanelucky.server.events.player.PlayerDropItemEvent
import org.kanelucky.server.events.player.PlayerPickupItemEvent

/**
 * @author Kanelucky
 */
object EventNodesRegistry {

    fun register(handler: GlobalEventHandler) {

        handler.addChild(BlockBreakEvent.node())

        handler.addChild(PlayerDropItemEvent.node())
        handler.addChild(PlayerPickupItemEvent.node())
        handler.addChild(PlayerDeathEvent.node())
    }
}