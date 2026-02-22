package org.kanelucky.server.commands

import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.minestom.LiteMinestomFactory

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.GameMode
import org.kanelucky.server.commands.argument.GameModeArgument

import org.kanelucky.server.commands.defaults.GameModeCommand
import org.kanelucky.server.commands.defaults.HelpCommand
import org.kanelucky.server.commands.defaults.KillCommand
import org.kanelucky.server.commands.defaults.OpCommand
import org.kanelucky.server.commands.defaults.StatusCommand
import org.kanelucky.server.commands.defaults.TPSCommand
import org.kanelucky.server.commands.defaults.VersionCommand
import org.kanelucky.server.commands.error.CommandInvalidUsageHandler
import org.kanelucky.server.commands.error.UnknownCommandHandler
import org.kanelucky.server.permission.handler.PermissionsHandler
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
                KillCommand(),
                HelpCommand(),
                StatusCommand()
            )
            .argument(GameMode::class.java, GameModeArgument())
            .invalidUsage(CommandInvalidUsageHandler())
            .missingPermission(PermissionsHandler())
            .build()
    }
}
