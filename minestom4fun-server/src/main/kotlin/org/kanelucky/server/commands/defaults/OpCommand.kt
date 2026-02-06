package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.permission.Permission

import net.minestom.server.entity.Player

/**
 * @author Kanelucky
 * Work in progress
 */
@Command(name = "op")
//@Permission("minestom4fun.commands.defaults.op")
class OpCommand {
    @Execute
    fun execute(
        @Context sender: Player,
        @Arg target: Player
    ) {
        target.permissionLevel = 4

        sender.sendMessage("§aMake ${target.username} OP")
        target.sendMessage("§6You have been OP")
    }
}