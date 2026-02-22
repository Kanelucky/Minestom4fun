package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix.COMMAND_DEFAULTS

/**
 * @author Kanelucky
 */
@Command(name = "gamemode", aliases = ["gm"])
@Permission("minestom4fun.commands.defaults.gamemode")
class GameModeCommand {
    @Execute
    fun self(
        @Context player: Player,
        @Arg("mode") mode: GameMode
    ) {
        player.setGameMode(mode)

        val msgMode = Component.text()
            .append(COMMAND_DEFAULTS)
            .append(Component.text("Gamemode set to ", NamedTextColor.GREEN))
            .append(Component.text(mode.name.lowercase(), NamedTextColor.YELLOW))
            .build()

        player.sendMessage(msgMode)
    }

    @Execute
    fun other(
        @Context sender: CommandSender,
        @Arg("mode") mode: GameMode,
        @Arg("target") target: Player
    ) {

        target.setGameMode(mode)

        val msgMode = Component.text()
            .append(COMMAND_DEFAULTS)
            .append(Component.text("Your GameModeCommand has been set to ", NamedTextColor.GREEN))
            .append(Component.text(mode.name.lowercase(), NamedTextColor.YELLOW))
            .build()

        val msgTarget = Component.text()
            .append(COMMAND_DEFAULTS)
            .append(Component.text("Set GameModeCommand of ", NamedTextColor.GREEN))
            .append(Component.text(target.username, NamedTextColor.YELLOW))
            .build()

        target.sendMessage(msgMode)
        sender.sendMessage(msgTarget)
    }
}
