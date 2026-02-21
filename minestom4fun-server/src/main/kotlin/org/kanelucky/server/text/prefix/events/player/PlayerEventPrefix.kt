package org.kanelucky.server.text.prefix.events.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * @author Kanelucky
 */
object PlayerEventPrefix {
    val EVENT_PLAYER: Component =
        Component.text("[Event/Player] ", NamedTextColor.YELLOW)
}