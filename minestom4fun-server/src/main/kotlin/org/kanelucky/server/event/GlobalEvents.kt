package org.kanelucky.event

import net.minestom.server.event.GlobalEventHandler

import org.kanelucky.event.player.PlayerJoinEvent
import org.kanelucky.event.player.PlayerQuitEvent

/**
 * @author Kanelucky
 */
object GlobalEvents {

    fun register(handler: GlobalEventHandler) {
        PlayerJoinEvent.register()
        PlayerQuitEvent.register()
    }
}