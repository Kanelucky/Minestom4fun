package org.kanelucky.event

import net.minestom.server.event.GlobalEventHandler

import org.kanelucky.event.block.BlockBreakEvent
import org.kanelucky.event.player.PlayerDeathEvent
import org.kanelucky.event.player.PlayerDropItemEvent
import org.kanelucky.event.player.PlayerPickupItemEvent

object EventNodes {

    fun register(handler: GlobalEventHandler) {

        handler.addChild(BlockBreakEvent.node())

        handler.addChild(PlayerDropItemEvent.node())
        handler.addChild(PlayerPickupItemEvent.node())
        handler.addChild(PlayerDeathEvent.node())
    }
}