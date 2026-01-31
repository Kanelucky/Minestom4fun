package org.kanelucky.event.player

import net.kyori.adventure.text.Component

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.MinecraftServer.LOGGER
import net.minestom.server.event.player.PlayerDisconnectEvent

import org.kanelucky.text.prefix.event.player.PlayerEventPrefix

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
                    .append(PlayerEventPrefix.EVENT_PLAYER)
                    .append(Component.text(player.username))
                    .append(Component.text(" left the game"))
                    .build()

                LOGGER.info(msg)

                MinecraftServer.getConnectionManager().onlinePlayers.forEach { it.sendMessage(msg) }
            }
    }
}