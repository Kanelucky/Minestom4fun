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
import net.minestom.server.utils.time.Tick

import org.kanelucky.server.config.ConfigManager

/**
 * @author Kanelucky
 */
@Command(name = "status")
@Description("Gets the current status of the server")
@Permission("minestom4fun.commands.defaults.status")
class StatusCommand {

    @Execute
    fun execute(@Context sender: CommandSender) {
        val onlinePlayers = MinecraftServer.getConnectionManager().onlinePlayers.size
        val maxPlayers = ConfigManager.serverSettings.maxPlayers
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
        val maxMemory = runtime.maxMemory() / 1024 / 1024
        val tps = Tick.SERVER_TICKS.ticksPerSecond

        sender.sendMessage(Component.text("--- Server Status ---", NamedTextColor.YELLOW))
        sender.sendMessage(
            Component.text()
                .append(Component.text("Players : ", NamedTextColor.GREEN))
                .append(Component.text("$onlinePlayers/$maxPlayers", NamedTextColor.YELLOW))
                .build()
        )
        sender.sendMessage(
            Component.text()
                .append(Component.text("Memory  : ", NamedTextColor.GREEN))
                .append(Component.text("${usedMemory}MB / ${maxMemory}MB", NamedTextColor.YELLOW))
                .build()
        )
        sender.sendMessage(
            Component.text()
                .append(Component.text("TPS     : ", NamedTextColor.GREEN))
                .append(Component.text("$tps", NamedTextColor.YELLOW))
                .build()
        )
    }
}