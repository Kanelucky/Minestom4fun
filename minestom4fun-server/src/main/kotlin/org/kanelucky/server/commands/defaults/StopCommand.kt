package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender

/**
 * @author Kanelucky
 */
@Command(name = "stop")
@Permission("minestom4fun.commands.defaults.stop")
class StopCommand {
    @Execute
    fun execute(@Context sender: CommandSender) {
        MinecraftServer.stopCleanly()
    }
}