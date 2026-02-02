package org.kanelucky.server.commands

import net.minestom.server.command.CommandSender

import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.minestom.LiteMinestomFactory

import org.kanelucky.server.commands.defaults.GameModeCommand
import org.kanelucky.server.commands.defaults.OpCommand
import org.kanelucky.server.commands.defaults.TPSCommand
import org.kanelucky.server.commands.defaults.VersionCommand
import org.kanelucky.server.permission.PermissionsHandler

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
            .missingPermission(PermissionsHandler())
            .build()
    }
}
