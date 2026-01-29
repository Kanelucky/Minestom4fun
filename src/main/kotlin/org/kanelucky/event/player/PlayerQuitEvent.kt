package org.kanelucky.event.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.MinecraftServer.LOGGER
import net.minestom.server.event.player.PlayerDisconnectEvent

object PlayerQuitEvent {
    @JvmStatic
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerDisconnectEvent::class.java) { event ->

                val player: Player = event.player

                val msg = Component.text()
                    .append(Component.text("[Event/Player] ", NamedTextColor.YELLOW))
                    .append(Component.text(player.username))
                    .append(Component.text(" left the game"))
                    .build()

                LOGGER.info(msg)

                MinecraftServer.getConnectionManager().onlinePlayers.forEach { it.sendMessage(msg) }
            }
    }
}