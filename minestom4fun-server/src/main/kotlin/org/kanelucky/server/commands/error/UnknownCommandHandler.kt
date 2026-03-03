package org.kanelucky.server.commands.error

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.MinecraftServer

import net.minestom.server.entity.Player

/**
 * @author Kanelucky
 */
object UnknownCommandHandler {

    fun register() {
        MinecraftServer.getCommandManager().setUnknownCommandCallback { sender, command ->
            sender.sendMessage(
                Component.text("Unknown or incomplete command, see below for error", NamedTextColor.RED)
            )
            sender.sendMessage(
                Component.text(command, NamedTextColor.RED)
                    .append(Component.text("<--[HERE]", NamedTextColor.RED).decorate(TextDecoration.ITALIC))
            )
            if (sender !is Player) {
                MinecraftServer.LOGGER.info("Unknown or incomplete command, see below for error")
                MinecraftServer.LOGGER.info("$command<--[HERE]")
            }
        }
    }
}