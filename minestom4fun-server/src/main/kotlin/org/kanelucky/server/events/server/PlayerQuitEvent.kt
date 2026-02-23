package org.kanelucky.server.events.server

import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerDisconnectEvent
import org.kanelucky.server.player.data.PlayerDataManager
import org.kanelucky.server.text.prefix.events.server.ServerEventPrefix

/**
 * @author Kanelucky
 */
object PlayerQuitEvent {
    @JvmStatic
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerDisconnectEvent::class.java) { event ->

                val player: Player = event.player

                val msg = Component.text()
                    .append(ServerEventPrefix.EVENT_SERVER)
                    .append(Component.text(player.username))
                    .append(Component.text(" left the game"))
                    .build()

                MinecraftServer.LOGGER.info(msg)

                MinecraftServer.getConnectionManager().onlinePlayers.forEach { it.sendMessage(msg) }

                PlayerDataManager.save(player)
            }
    }
}