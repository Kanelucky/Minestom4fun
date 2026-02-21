package org.kanelucky.server.commands.error

import dev.rollczi.litecommands.handler.result.ResultHandlerChain
import dev.rollczi.litecommands.invalidusage.InvalidUsage
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler
import dev.rollczi.litecommands.invocation.Invocation
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.CommandSender
import org.kanelucky.server.text.prefix.commands.error.ErrorCommandPrefix.COMMAND_ERROR

/**
 * @author Kanelucky
 */
class CommandInvalidUsageHandler : InvalidUsageHandler<CommandSender> {
    override fun handle(
        invocation: Invocation<CommandSender>,
        result: InvalidUsage<CommandSender>,
        chain: ResultHandlerChain<CommandSender>
    ) {
        invocation.sender().sendMessage(
            Component.text()
                .append(COMMAND_ERROR)
                .append(Component.text("Invalid usage: ", NamedTextColor.RED))
                .append(Component.text(result.schematic.first(), NamedTextColor.YELLOW))
                .build()
        )
    }
}