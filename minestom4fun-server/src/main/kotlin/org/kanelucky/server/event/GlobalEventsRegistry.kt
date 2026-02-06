package org.kanelucky.server.event

import net.minestom.server.event.GlobalEventHandler
import org.kanelucky.server.event.player.PlayerJoinEvent
import org.kanelucky.server.event.player.PlayerQuitEvent
import org.kanelucky.server.event.player.PlayerSpawnEvent


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