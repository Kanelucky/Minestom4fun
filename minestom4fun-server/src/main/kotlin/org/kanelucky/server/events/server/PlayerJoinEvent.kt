package org.kanelucky.server.events.server

import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerSpawnEvent
import org.kanelucky.server.text.prefix.event.player.PlayerEventPrefix

/**
 * @author Kanelucky
 */
object PlayerJoinEvent {
    @JvmStatic
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerSpawnEvent::class.java) { event ->

                if (!event.isFirstSpawn) return@addListener

                val player: Player = event.player

                val msg = Component.text()
                    .append(PlayerEventPrefix.EVENT_PLAYER)
                    .append(Component.text(player.username))
                    .append(Component.text(" joined the game"))
                    .build()

                MinecraftServer.LOGGER.info(msg)

                MinecraftServer.getConnectionManager().onlinePlayers.forEach { it.sendMessage(msg) }
            }
    }
}