package org.kanelucky.server.commands

import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.message.LiteMessages
import dev.rollczi.litecommands.minestom.LiteMinestomFactory
import dev.rollczi.litecommands.permission.PermissionResolver
import dev.rollczi.litecommands.permission.PermissionValidationResult

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

import org.kanelucky.server.commands.defaults.GameModeCommand
import org.kanelucky.server.commands.defaults.KillCommand
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
            .permissionResolver(PermissionResolver.createDefault(CommandSender::class.java) { sender, permission ->
                if (sender is Player) {
                    sender.permissionLevel >= 4
                } else {
                    true
                }
            })
            .commands(
                GameModeCommand(),
                VersionCommand(),
                TPSCommand(),
                OpCommand(),
                KillCommand()
            )

            .missingPermission(PermissionsHandler())
            .build()
    }
}
