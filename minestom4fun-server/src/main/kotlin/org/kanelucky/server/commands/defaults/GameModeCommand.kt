package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.description.Description
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

/**
 * @author Kanelucky
 */
@Command(name = "gamemode")
@Description("Sets a player's game mode")
@Permission("minestom4fun.commands.defaults.gamemode")
class GameModeCommand {

    private fun GameMode.formatted() = name.lowercase().replaceFirstChar { it.uppercase() }

    @Execute
    fun self(
        @Context player: Player,
        @Arg("mode") mode: GameMode
    ) {
        player.setGameMode(mode)

        val msgMode = "Set own game mode to ${mode.formatted()} Mode"

        player.sendMessage(msgMode)
    }

    @Execute
    fun other(
        @Context sender: CommandSender,
        @Arg("mode") mode: GameMode,
        @Arg("target") target: Player
    ) {

        target.setGameMode(mode)

        val formattedMode = mode.name.substring(0, 1).uppercase() + mode.name.substring(1).lowercase()

        val msgTarget = "Your game mode has been updated to ${mode.formatted()} Mode"

        val msgMode = "Set ${target.username}'s game mode to ${mode.formatted()} Mode"

        target.sendMessage(msgTarget)
        sender.sendMessage(msgMode)
    }
}
