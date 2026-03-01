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
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix.COMMAND_DEFAULTS

/**
 * @author Kanelucky
 */
@Command(name = "gamemode")
@Description("Sets a player's game mode")
@Permission("minestom4fun.commands.defaults.gamemode")
class GameModeCommand {
    @Execute
    fun self(
        @Context player: Player,
        @Arg("mode") mode: GameMode
    ) {
        player.setGameMode(mode)

        val formattedMode = mode.name.substring(0, 1).uppercase() + mode.name.substring(1).lowercase()

        val msgMode = Component.text()
            .append(Component.text("Set own game mode to "))
            .append(Component.text(formattedMode))
            .append(Component.text(" Mode"))
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

        val formattedMode = mode.name.substring(0, 1).uppercase() + mode.name.substring(1).lowercase()

        val msgTarget = Component.text()
            .append(Component.text("Your game mode has been updated to "))
            .append(Component.text(formattedMode))
            .append(Component.text(" Mode"))
            .build()

        val msgMode = "Set ${target.username}'s game mode to $formattedMode Mode"

        target.sendMessage(msgTarget)
        sender.sendMessage(msgMode)
    }
}
