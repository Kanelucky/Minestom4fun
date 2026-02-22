package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.command.CommandSender

import org.kanelucky.server.commands.CommandList.commands

/**
 * @author Kanelucky
 */
@Command(name = "help", aliases = ["?"])
@Permission("minestom4fun.commands.defaults.help")
class HelpCommand {

    @Execute
    fun execute(@Context sender: CommandSender) {
        sender.sendMessage(
            Component.text("--- Showing help page ---", NamedTextColor.YELLOW)
        )

        commands.forEach { (cmd, desc) ->
            sender.sendMessage(
                Component.text()
                    .append(Component.text(cmd, NamedTextColor.GREEN))
                    .append(Component.text(": ", NamedTextColor.WHITE))
                    .append(Component.text(desc, NamedTextColor.WHITE))
                    .build()
            )
        }
    }
}