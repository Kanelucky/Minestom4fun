package org.kanelucky.commands

import net.minestom.server.command.CommandSender

import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.minestom.LiteMinestomFactory

import org.kanelucky.commands.defaults.GameModeCommand
import org.kanelucky.commands.defaults.TPSCommand
import org.kanelucky.commands.defaults.VersionCommand

/**
 * @author Kanelucky
 */
object CommandRegistry {

    fun initialize(): LiteCommands<CommandSender> {
        return LiteMinestomFactory.builder()
            .commands(
                GameModeCommand(),
                VersionCommand(),
                TPSCommand()
            )
            .build()
    }
}
