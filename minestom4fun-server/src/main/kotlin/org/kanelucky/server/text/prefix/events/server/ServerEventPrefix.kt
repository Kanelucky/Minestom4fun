package org.kanelucky.server.text.prefix.events.server

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * @author Kanelucky
 */
object ServerEventPrefix {
    val EVENT_SERVER: Component =
        Component.text("[Event/Server] ", NamedTextColor.YELLOW)
}