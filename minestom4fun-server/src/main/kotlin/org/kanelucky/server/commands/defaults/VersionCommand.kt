package org.kanelucky.commands.defaults

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute

import net.kyori.adventure.text.Component

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender

import org.kanelucky.text.prefix.commands.defaults.DefaultCommandPrefix

/**
 * @author Kanelucky
 */
@Command(name = "version", aliases = ["ver"])
class VersionCommand {
    @Execute
    fun execute(@Context sender: CommandSender ) {
        val version: String =
            this::class.java.`package`.implementationVersion ?: "dev"

        val minestomVersion = MinecraftServer.VERSION_NAME

        val msg = Component.text()
            .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
            .append(Component.text("§aMinestom4fun §7v${version} "))
            .append(Component.text("§9(Minestom ${minestomVersion})"))
            .build()

        sender.sendMessage(msg)
    }
}