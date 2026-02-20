package org.kanelucky.server.events.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent

import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerDeathEvent

import org.kanelucky.server.text.prefix.event.player.PlayerEventPrefix

/**
 * @author Kanelucky
 */
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

                    "death.attack.inFire" ->
                        "${player.username} went up in flames"

                    "death.attack.onFire" ->
                        "${player.username} burned to death"

                    "death.attack.fire" ->
                        "${player.username} burned to death"

                    "death.attack.wither" ->
                        "${player.username} withered away"

                    "death.attack.cactus" ->
                        "${player.username} was pricked to death"

                    "death.attack.sweetBerryBush" ->
                        "${player.username} was poked to death by a sweet berry bush"

                    "death.attack.sting" ->
                        "${player.username} was stung to death"

                    else ->
                        "${player.username} died (${deathText.key()})"
                }
            } else {
                "${player.username} died"
            }

            val msg = Component.text()
                .append(PlayerEventPrefix.EVENT_PLAYER)
                .append(Component.text("$message"))
                .build()

            MinecraftServer.LOGGER.info(msg)
        }

        return node
    }
}