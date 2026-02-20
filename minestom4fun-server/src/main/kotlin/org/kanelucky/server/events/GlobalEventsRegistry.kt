package org.kanelucky.server.events

import net.minestom.server.event.GlobalEventHandler
import org.kanelucky.server.events.player.PlayerJoinEvent
import org.kanelucky.server.events.player.PlayerQuitEvent
import org.kanelucky.server.events.player.PlayerSpawnEvent


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