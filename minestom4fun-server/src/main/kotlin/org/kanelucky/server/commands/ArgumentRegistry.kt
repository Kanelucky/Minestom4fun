package org.kanelucky.server.commands

import dev.rollczi.litecommands.LiteCommandsBuilder

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.GameMode
import net.minestom.server.item.Material

import org.kanelucky.server.commands.argument.GameModeArgument
import org.kanelucky.server.commands.argument.MaterialArgument

/**
 * @author Kanelucky
 */
object ArgumentRegistry {

    fun register(builder: LiteCommandsBuilder<CommandSender, *, *>) {
        builder
            .argument(GameMode::class.java, GameModeArgument())
            .argument(Material::class.java, MaterialArgument())
    }
}