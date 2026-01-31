package org.kanelucky.server.event

import net.minestom.server.event.GlobalEventHandler

import org.kanelucky.server.event.block.BlockBreakEvent
import org.kanelucky.server.event.player.PlayerDeathEvent
import org.kanelucky.server.event.player.PlayerDropItemEvent
import org.kanelucky.server.event.player.PlayerPickupItemEvent

/**
 * @author Kanelucky
 */
object EventNodes {

    fun register(handler: GlobalEventHandler) {

        handler.addChild(BlockBreakEvent.node())

        handler.addChild(PlayerDropItemEvent.node())
        handler.addChild(PlayerPickupItemEvent.node())
        handler.addChild(PlayerDeathEvent.node())
    }
}