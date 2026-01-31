package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

import net.minestom.server.command.CommandSender
import net.minestom.server.utils.time.Tick

import org.kanelucky.server.text.prefix.commands.defaults.DefaultCommandPrefix

/**
 * @author Kanelucky
 */
@Command(name = "tps")
class TPSCommand {
    @Execute
    fun execute(@Context sender: CommandSender) {
        val tps = Tick.SERVER_TICKS.ticksPerSecond
        if (tps >= 19) {
            val msg = Component.text()
                .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
                .append(Component.text("TPS: ", NamedTextColor.YELLOW))
                .append(Component.text("$tps", NamedTextColor.GREEN))
                .build()
            sender.sendMessage(msg)
        } else if (tps <= 18 && tps >= 17) {
            val msg = Component.text()
                .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
                .append(Component.text("TPS: ", NamedTextColor.YELLOW))
                .append(Component.text("$tps", NamedTextColor.YELLOW))
                .build()
            sender.sendMessage(msg)
        } else {
            val msg = Component.text()
                .append(DefaultCommandPrefix.COMMAND_DEFAULTS)
                .append(Component.text("TPS: ", NamedTextColor.YELLOW))
                .append(Component.text("$tps", NamedTextColor.RED))
                .build()
            sender.sendMessage(msg)
        }
    }
}