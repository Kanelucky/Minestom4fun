package org.kanelucky.server.commands.argument

import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.argument.parser.ParseResult
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import net.minestom.server.command.CommandSender
import net.minestom.server.item.Material

/**
 * @author Kanelucky
 */
class MaterialArgument : ArgumentResolver<CommandSender, Material>() {

    override fun parse(
        invocation: Invocation<CommandSender>,
        argument: Argument<Material>,
        context: String
    ): ParseResult<Material> {
        val material = Material.fromKey(context)
            ?: return ParseResult.failure("Unknown item: $context")
        return ParseResult.success(material)
    }

    override fun suggest(
        invocation: Invocation<CommandSender>,
        argument: Argument<Material>,
        context: SuggestionContext
    ): SuggestionResult {
        return SuggestionResult.of(Material.values().map { it.name() })
    }
}