package org.kanelucky.server.commands.argument

import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import dev.rollczi.litecommands.argument.parser.ParseResult
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.GameMode

/**
 * @author Kanelucky
 */
class GameModeArgument : ArgumentResolver<CommandSender, GameMode>() {

    override fun parse(
        invocation: Invocation<CommandSender>,
        argument: Argument<GameMode>,
        ctx: String
    ): ParseResult<GameMode> {
        val mode = when (ctx.lowercase()) {
            "survival", "s", "0" -> GameMode.SURVIVAL
            "creative", "c", "1" -> GameMode.CREATIVE
            "adventure", "a", "2" -> GameMode.ADVENTURE
            "spectator", "sp", "3" -> GameMode.SPECTATOR
            else -> return ParseResult.failure("Unknown gamemode: $ctx")
        }
        return ParseResult.success(mode)
    }

    override fun suggest(
        invocation: Invocation<CommandSender>,
        argument: Argument<GameMode>,
        ctx: SuggestionContext
    ): SuggestionResult {
        return SuggestionResult.of("survival", "creative", "adventure", "spectator")
    }
}