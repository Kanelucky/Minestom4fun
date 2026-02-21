package org.kanelucky.server.permission

import dev.rollczi.litecommands.handler.result.ResultHandlerChain
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.permission.MissingPermissions
import dev.rollczi.litecommands.permission.MissingPermissionsHandler

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.command.CommandSender

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix

class PermissionsHandler : MissingPermissionsHandler<CommandSender> {
    override fun handle(
        invocation: Invocation<CommandSender>,
        missingPermissions: MissingPermissions,
        chain: ResultHandlerChain<CommandSender>
    ) {
        val permissions = missingPermissions.asJoinedText()
        val sender: CommandSender = invocation.sender()

        val msg = Component.text()
            .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
            .append(Component.text("Required permissions: ", NamedTextColor.GREEN))
            .append(Component.text("(${permissions})", NamedTextColor.YELLOW))
            .build()

        sender.sendMessage(msg)
    }
}