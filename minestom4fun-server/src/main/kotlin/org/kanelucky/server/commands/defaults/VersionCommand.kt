package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix

/**
 * @author Kanelucky
 */
@Command(name = "version", aliases = ["ver"])
//@Permission("minestom4fun.commands.defaults.version")
class VersionCommand {
    @Execute
    fun execute(@Context sender: CommandSender ) {

        val version: String = this::class.java.`package`.implementationVersion ?: "dev"

        val minestomVersion = MinecraftServer.VERSION_NAME

        val msg = Component.text()
            .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
            .append(Component.text("Minestom4fun ", NamedTextColor.GREEN))
            .append(Component.text("v${version} ", NamedTextColor.GRAY))
            .append(Component.text("(Minestom ${minestomVersion})", NamedTextColor.BLUE))
            .build()

        sender.sendMessage(msg)
    }
}