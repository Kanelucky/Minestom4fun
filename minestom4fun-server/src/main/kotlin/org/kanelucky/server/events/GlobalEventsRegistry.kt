package org.kanelucky.server.events

import net.minestom.server.event.GlobalEventHandler

import org.kanelucky.server.events.server.PlayerJoinEvent
import org.kanelucky.server.events.server.PlayerQuitEvent
import org.kanelucky.server.events.server.PlayerSpawnEvent

/**
 * @author Kanelucky
 */
object GlobalEventsRegistry {

    fun register(handler: GlobalEventHandler) {
        PlayerJoinEvent.register()
        PlayerQuitEvent.register()
        PlayerSpawnEvent.register()
    }
}