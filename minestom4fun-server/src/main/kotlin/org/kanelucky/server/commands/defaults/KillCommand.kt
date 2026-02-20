package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Player

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix

@Command(name = "kill")
class KillCommand {

    fun execute(
        sender: Player,
        @Arg target: Player
    ) {

        target.health = 0f

        val message = Component.text()
            .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
            .append(Component.text("Killed ${target.username}", NamedTextColor.GREEN))
            .build()

        sender.sendMessage(message)
    }

}