package org.kanelucky.server.text.prefix.commands.defaults

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * @author Kanelucky
 */
object DefaultCommandPrefix {
    val COMMAND_DEFAULTS: Component =
        Component.text("[Commands/Default] ", NamedTextColor.YELLOW)
    val COMMAND_ERROR: Component =
        Component.text("[Commands/Error] ", NamedTextColor.YELLOW)
}