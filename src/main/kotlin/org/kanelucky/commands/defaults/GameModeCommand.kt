package org.kanelucky.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute

import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player


@Command(name = "gamemode", aliases = ["gm"])
class GameModeCommand {
    @Execute
    fun self(
        @Context player: Player,
        @Arg mode: GameMode
    ) {
        player.setGameMode(mode)
        player.sendMessage("§aGamemode set to §e" + mode.name)
    }

    @Execute
    fun other(
        @Context sender: Player,
        @Arg mode: GameMode,
        @Arg target: Player
    ) {

        target.setGameMode(mode)

        target.sendMessage("§aYour GameModeCommand has been set to §e" + mode.name)
        sender.sendMessage("§aSet GameModeCommand of §e" + target.getUsername())
    }
}
