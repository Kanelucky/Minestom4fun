package org.kanelucky.commands.defaults

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute

import net.minestom.server.command.CommandSender

@Command(name = "version", aliases = ["ver"])
class VersionCommand {
    @Execute
    fun execute(@Context sender: CommandSender ) {
        val version: String =
            this::class.java.`package`.implementationVersion ?: "dev"
        sender.sendMessage("§aMinestom4fun v${version}")
    }
}