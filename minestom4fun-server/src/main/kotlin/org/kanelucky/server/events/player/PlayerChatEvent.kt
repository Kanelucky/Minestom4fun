package org.kanelucky.server.events.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerChatEvent

/**
 * @author Kanelucky
 */
object PlayerChatEvent {

    @JvmStatic
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerChatEvent::class.java) { event ->

                val message = Component.text("<")
                    .append(Component.text(event.player.username, NamedTextColor.WHITE))
                    .append(Component.text("> "))
                    .append(Component.text(event.rawMessage))

                event.formattedMessage = message
                MinecraftServer.LOGGER.info(message)
            }
    }
}