package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.description.Description
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix.COMMAND_DEFAULTS

/**
 * @author Kanelucky
 */
@Command(name = "version", aliases = ["ver"])
@Description("Gets the version of this server including any plugins in use")
@Permission("minestom4fun.commands.defaults.version")
class VersionCommand {

    val version: String = this::class.java.`package`.implementationVersion ?: "dev"

    @Execute
    fun execute(
        @Context sender: CommandSender
    ) {

        val minestomVersion = MinecraftServer.VERSION_NAME

        val msg = Component.text()
            .append(COMMAND_DEFAULTS)
            .append(Component.text("Minestom4fun ", NamedTextColor.GREEN))
            .append(Component.text("v${version} ", NamedTextColor.GRAY))
            .append(Component.text("(Minestom ${minestomVersion})", NamedTextColor.BLUE))
            .build()

        sender.sendMessage(msg)
    }
}