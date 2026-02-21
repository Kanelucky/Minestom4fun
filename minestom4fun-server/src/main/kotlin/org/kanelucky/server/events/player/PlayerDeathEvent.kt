package org.kanelucky.server.events.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent

import net.minestom.server.MinecraftServer
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerDeathEvent

import org.kanelucky.server.text.prefix.events.player.PlayerEventPrefix

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

                    "death.attack.fall"             -> "${player.username} fell from a high place"
                    "death.attack.outOfWorld"       -> "${player.username} fell out of the world"
                    "death.attack.fallingBlock"     -> "${player.username} was squashed by a falling block"

                    "death.attack.player"           -> "${player.username} was slain by another player"
                    "death.attack.arrow"            -> "${player.username} was shot by an arrow"
                    "death.attack.thrown"           -> "${player.username} was knocked into the void"
                    "death.attack.mob"              -> "${player.username} was killed by a mob"

                    "death.attack.inFire"           -> "${player.username} went up in flames"
                    "death.attack.onFire"           -> "${player.username} burned to death"
                    "death.attack.fire"             -> "${player.username} burned to death"
                    "death.attack.lava"             -> "${player.username} tried to swim in lava"
                    "death.attack.hotFloor"         -> "${player.username} discovered the floor was lava"

                    "death.attack.drown"            -> "${player.username} drowned"
                    "death.attack.dryout"           -> "${player.username} died from dehydration"

                    "death.attack.explosion"        -> "${player.username} blew up"
                    "death.attack.badRespawnPoint"  -> "${player.username}'s respawn point was obstructed"

                    "death.attack.cactus"           -> "${player.username} was pricked to death"
                    "death.attack.sweetBerryBush"   -> "${player.username} was poked to death by a sweet berry bush"
                    "death.attack.wither"           -> "${player.username} withered away"
                    "death.attack.sting"            -> "${player.username} was stung to death"
                    "death.attack.starve"           -> "${player.username} starved to death"
                    "death.attack.magic"            -> "${player.username} was killed by magic"
                    "death.attack.lightningBolt"    -> "${player.username} was struck by lightning"
                    "death.attack.flyIntoWall"      -> "${player.username} experienced kinetic energy"
                    "death.attack.anvil"            -> "${player.username} was squashed by a falling anvil"
                    "death.attack.dragonBreath"     -> "${player.username} was roasted in dragon's breath"
                    "death.attack.freeze"           -> "${player.username} froze to death"
                    "death.attack.stalagmite"       -> "${player.username} was impaled on a stalagmite"
                    "death.attack.cramming"         -> "${player.username} was squished too much"

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