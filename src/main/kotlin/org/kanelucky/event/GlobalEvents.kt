package org.kanelucky.event

import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import org.kanelucky.event.player.PlayerJoinEvent
import org.kanelucky.event.player.PlayerQuitEvent

object GlobalEvents {

    fun register(handler: GlobalEventHandler) {
        PlayerJoinEvent.register()
        PlayerQuitEvent.register()
    }
}