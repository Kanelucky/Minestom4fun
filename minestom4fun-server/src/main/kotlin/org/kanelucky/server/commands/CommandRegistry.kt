package org.kanelucky.server.commands

import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.minestom.LiteMinestomFactory

import net.minestom.server.command.CommandSender

import org.kanelucky.server.commands.defaults.GameModeCommand
import org.kanelucky.server.commands.defaults.KillCommand
import org.kanelucky.server.commands.defaults.OpCommand
import org.kanelucky.server.commands.defaults.TPSCommand
import org.kanelucky.server.commands.defaults.VersionCommand
import org.kanelucky.server.commands.error.CommandInvalidUsageHandler
import org.kanelucky.server.commands.error.UnknownCommandHandler
import org.kanelucky.server.permission.PermissionsHandler
import org.kanelucky.server.permission.ServerPermissionResolver

/**
 * @author Kanelucky
 */
object CommandRegistry {

    fun initialize(): LiteCommands<CommandSender> {
        UnknownCommandHandler.register()
        return LiteMinestomFactory.builder()
            .permissionResolver(ServerPermissionResolver())
            .commands(
                GameModeCommand(),
                VersionCommand(),
                TPSCommand(),
                OpCommand(),
                KillCommand()
            )
            .invalidUsage(CommandInvalidUsageHandler())
            .missingPermission(PermissionsHandler())
            .build()
    }
}
