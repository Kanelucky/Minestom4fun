package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.description.Description
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission
import dev.rollczi.litecommands.meta.Meta

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.command.CommandSender

import org.kanelucky.server.commands.CommandRegistry

/**
 * @author Kanelucky
 */
@Command(name = "help", aliases = ["?"])
@Description("Lists server commands or provides help for a command")
@Permission("minestom4fun.commands.defaults.help")
class HelpCommand {

    @Execute
    fun execute(@Context sender: CommandSender) {
        sender.sendMessage(
            Component.text("--- Showing help page ---", NamedTextColor.YELLOW)
        )

        CommandRegistry.liteCommands.commandManager.root.getChildren()
            .sortedBy { it.getName() }
            .forEach { route ->
            val description = route.meta().get(Meta.DESCRIPTION).firstOrNull() ?: ""
            sender.sendMessage(
                Component.text()
                    .append(Component.text("/${route.getName()}", NamedTextColor.GREEN))
                    .append(Component.text(": ", NamedTextColor.WHITE))
                    .append(Component.text(description, NamedTextColor.WHITE))
                    .build()
            )
        }
    }
}