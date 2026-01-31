package org.kanelucky.network.status

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.MinecraftServer
import net.minestom.server.event.server.ServerListPingEvent
import net.minestom.server.ping.Status

/**
 * @author Kanelucky
 */
object ServerListPing {
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(ServerListPingEvent::class.java) { event ->
                val onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayerCount()
                val version: String =
                    this::class.java.`package`.implementationVersion ?: "dev"
                event.setStatus(Status.builder()
                    .description(Component.text("Welcome to my Minecraft server!", NamedTextColor.GOLD))
                    .playerInfo(Status.PlayerInfo.builder()
                        .onlinePlayers(onlinePlayers)
                        .maxPlayers(onlinePlayers)
                        .build())
                    .versionInfo(Status.VersionInfo("§aMinestom4fun §7v${version}", 774))
                    .build());
            }
    }
}
