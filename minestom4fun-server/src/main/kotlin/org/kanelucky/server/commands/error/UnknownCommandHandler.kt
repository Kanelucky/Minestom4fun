package org.kanelucky.server.commands.error

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import org.kanelucky.server.text.prefix.commands.error.ErrorCommandPrefix.COMMAND_ERROR

/**
 * @author Kanelucky
 */
object UnknownCommandHandler {

    fun register() {
        MinecraftServer.getCommandManager().setUnknownCommandCallback { sender, command ->
            sender.sendMessage(
                Component.text()
                    .append(COMMAND_ERROR)
                    .append(Component.text("Unknown command: ", NamedTextColor.RED))
                    .append(Component.text("/$command", NamedTextColor.YELLOW))
                    .build()
            )
        }
    }
}