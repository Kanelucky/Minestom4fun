package org.kanelucky.server.text.prefix.events.block

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * @author Kanelucky
 */
object BlockEventPrefix {
    val EVENT_BLOCK: Component =
        Component.text("[Event/Block] ", NamedTextColor.YELLOW)
}