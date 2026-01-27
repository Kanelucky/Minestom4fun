package org.kanelucky.event.player

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerSpawnEvent

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

object PlayerJoinEvent {
    @JvmStatic
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerSpawnEvent::class.java) { event ->

                if (!event.isFirstSpawn) return@addListener

                val player: Player = event.player
                val playerName = player.username

                val msg = Component.text()
                    .append(Component.text("[Event/Player] ", NamedTextColor.YELLOW))
                    .append(Component.text(playerName, NamedTextColor.WHITE))
                    .append(Component.text(" joined the game", NamedTextColor.WHITE))
                    .build()

                println(
                    PlainTextComponentSerializer.plainText().serialize(msg)
                )

                MinecraftServer.getConnectionManager().onlinePlayers.forEach { it.sendMessage(msg) }
            }
    }
}