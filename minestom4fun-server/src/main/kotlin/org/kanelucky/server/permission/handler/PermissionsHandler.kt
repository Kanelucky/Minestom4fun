package org.kanelucky.server.permission.handler

import dev.rollczi.litecommands.handler.result.ResultHandlerChain
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.permission.MissingPermissions
import dev.rollczi.litecommands.permission.MissingPermissionsHandler

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.command.CommandSender

import org.kanelucky.server.text.prefix.commands.error.ErrorCommandPrefix

/**
 * @author Kanelucky
 */
class PermissionsHandler : MissingPermissionsHandler<CommandSender> {
    override fun handle(
        invocation: Invocation<CommandSender>,
        missingPermissions: MissingPermissions,
        chain: ResultHandlerChain<CommandSender>
    ) {
        invocation.sender().sendMessage(
            Component.text()
                .append(ErrorCommandPrefix.COMMAND_ERROR)
                .append(Component.text("You don't have permission to use this command", NamedTextColor.RED))
                .build()
        )
    }
}