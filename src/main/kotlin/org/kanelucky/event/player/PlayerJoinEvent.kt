package org.kanelucky.event.player

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerSpawnEvent

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer.LOGGER

object PlayerJoinEvent {
    @JvmStatic
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerSpawnEvent::class.java) { event ->

                if (!event.isFirstSpawn) return@addListener

                val player: Player = event.player

                val msg = Component.text()
                    .append(Component.text("[Event/Player] ", NamedTextColor.YELLOW))
                    .append(Component.text(player.username))
                    .append(Component.text(" joined the game"))
                    .build()

                LOGGER.info(msg)

                MinecraftServer.getConnectionManager().onlinePlayers.forEach { it.sendMessage(msg) }
            }
    }
}