package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.description.Description
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player


/**
 * @author Kanelucky
 */
@Command(name = "kill")
@Description("Kills entities (players, mobs, items, etc.)")
@Permission("minestom4fun.commands.defaults.kill")
class KillCommand {

    @Execute
    fun self(
        @Context sender: Player
    ) {
        sender.kill()
        val killed = "Killed ${sender.username}"
        sender.sendMessage("${sender.username} was killed")
        sender.sendMessage(killed)
        MinecraftServer.LOGGER.info("${sender.username} was killed")
        MinecraftServer.LOGGER.info(
            Component.text()
                .append(Component.text("[${sender.username}: $killed]").decorate(TextDecoration.ITALIC))
                .build()
        )
    }

    @Execute
    fun other(
        @Context sender: CommandSender,
        @Arg("target") target: Player
    ) {
        target.kill()
        val killed = "Killed ${target.username}"
        val senderName = if (sender is Player) sender.username else "Console"
        target.sendMessage("${target.username} was killed")
        target.sendMessage("[$senderName: $killed]")
        MinecraftServer.LOGGER.info("${target.username} was killed")
        MinecraftServer.LOGGER.info(killed)
    }
}