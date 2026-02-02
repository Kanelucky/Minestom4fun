package org.kanelucky.server.permission

import dev.rollczi.litecommands.handler.result.ResultHandlerChain
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.permission.MissingPermissions
import dev.rollczi.litecommands.permission.MissingPermissionsHandler

import net.minestom.server.command.CommandSender

class PermissionsHandler : MissingPermissionsHandler<CommandSender> {
    override fun handle(
        invocation: Invocation<CommandSender>,
        missingPermissions: MissingPermissions,
        chain: ResultHandlerChain<CommandSender>
    ) {
        val permissions = missingPermissions.asJoinedText()
        val sender: CommandSender = invocation.sender()

        sender.sendMessage("Required permissions: (" + permissions + ")")
    }
}