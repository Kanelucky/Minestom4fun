package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.description.Description
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix.COMMAND_DEFAULTS

/**
 * @author Kanelucky
 */
@Command(name = "op")
@Description("Grants operator status to a player")
@Permission("minestom4fun.commands.defaults.op")
class OpCommand {
    @Execute
    fun execute(
        @Context sender: CommandSender,
        @Arg("target") target: Player
    ) {
        target.permissionLevel = 4

        val msgSender = Component.text()
            .append(COMMAND_DEFAULTS)
            .append(Component.text("Make ", NamedTextColor.GREEN))
            .append(Component.text("${target.username}", NamedTextColor.YELLOW))
            .append(Component.text(" OP", NamedTextColor.GREEN))
            .build()

        val msgTarget = Component.text()
            .append(COMMAND_DEFAULTS)
            .append(Component.text("You have been OP", NamedTextColor.GREEN))
            .build()

        sender.sendMessage(msgSender)
        target.sendMessage(msgTarget)
    }
}