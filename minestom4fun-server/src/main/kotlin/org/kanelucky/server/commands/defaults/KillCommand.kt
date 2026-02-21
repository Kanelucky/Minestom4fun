package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Sender
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix.COMMAND_DEFAULTS

@Command(name = "kill")
@Permission("minestom4fun.commands.defaults.kill")
class KillCommand {

    @Execute
    fun self(
        @Sender sender: Player
    ) {
        sender.kill()

        val message = Component.text()
            .append(COMMAND_DEFAULTS)
            .append(Component.text("Killed ", NamedTextColor.GREEN))
            .append(Component.text("${sender.username}", NamedTextColor.YELLOW))
            .build()

        sender.sendMessage(message)
    }

    @Execute
    fun other(
        @Sender sender: Player,
        @Arg("target") target: Player
    ) {
        target.kill()

        val message = Component.text()
            .append(COMMAND_DEFAULTS)
            .append(Component.text("Killed ", NamedTextColor.GREEN))
            .append(Component.text("${target.username}", NamedTextColor.YELLOW))
            .build()

        sender.sendMessage(message)
    }
}