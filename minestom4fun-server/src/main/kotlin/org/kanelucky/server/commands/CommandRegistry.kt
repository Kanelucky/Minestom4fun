package org.kanelucky.server.commands

import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.minestom.LiteMinestomFactory

import net.minestom.server.command.CommandSender

import org.kanelucky.server.commands.defaults.GameModeCommand
import org.kanelucky.server.commands.defaults.OpCommand
import org.kanelucky.server.commands.defaults.TPSCommand
import org.kanelucky.server.commands.defaults.VersionCommand

/**
 * @author Kanelucky
 */
object CommandRegistry {

    fun initialize(): LiteCommands<CommandSender> {
        return LiteMinestomFactory.builder()
            .commands(
                GameModeCommand(),
                VersionCommand(),
                TPSCommand(),
                OpCommand()
            )
            .build()
    }
}
