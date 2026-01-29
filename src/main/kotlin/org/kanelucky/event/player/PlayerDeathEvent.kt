package org.kanelucky.event.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerDeathEvent

object PlayerDeathEvent {
    @JvmStatic
    fun node(): EventNode<Event> {

        val node = EventNode.all("player-death")

        node.addListener(PlayerDeathEvent::class.java) { event ->
            val player = event.entity
            val deathText = event.deathText ?: return@addListener

            val message = if (deathText is TranslatableComponent) {
                when (deathText.key()) {
                    "death.attack.fall" ->
                        "${player.username} fell from a high place"

                    "death.attack.player" ->
                        "${player.username} was slain by another player"

                    "death.attack.lava" ->
                        "${player.username} tried to swim in lava"

                    else ->
                        "${player.username} died (${deathText.key()})"
                }
            } else {
                "${player.username} died"
            }

            val msg = Component.text()
                .append(Component.text("[Event/Player] ", NamedTextColor.YELLOW))
                .append(Component.text("$message"))
                .build()

            MinecraftServer.LOGGER.info(msg)
        }

        return node
    }
}