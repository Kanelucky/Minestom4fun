package org.kanelucky.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import net.kyori.adventure.text.Component

import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import org.kanelucky.text.prefix.commands.defaults.DefaultCommandPrefix

/**
 * @author Kanelucky
 */
@Command(name = "gamemode", aliases = ["gm"])
class GameModeCommand {
    @Execute
    fun self(
        @Context player: Player,
        @Arg mode: GameMode
    ) {
        player.setGameMode(mode)

        val msgMode = Component.text()
            .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
            .append(Component.text("§aGamemode set to §e"))
            .append(Component.text(mode.name))
            .build()

        player.sendMessage(msgMode)
    }

    @Execute
    fun other(
        @Context sender: Player,
        @Arg mode: GameMode,
        @Arg target: Player
    ) {

        target.setGameMode(mode)

        val msgMode = Component.text()
            .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
            .append(Component.text("§aYour GameModeCommand has been set to §e"))
            .append(Component.text(mode.name))
            .build()

        val msgTarget = Component.text()
            .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
            .append(Component.text("§aSet GameModeCommand of §e"))
            .append(Component.text(target.username))
            .build()

        target.sendMessage(msgMode)
        sender.sendMessage(msgTarget)
    }
}
