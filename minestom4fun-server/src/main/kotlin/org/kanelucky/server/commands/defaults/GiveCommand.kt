package org.kanelucky.server.commands.defaults

import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.description.Description
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.optional.OptionalArg
import dev.rollczi.litecommands.annotations.permission.Permission

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import net.minestom.server.inventory.PlayerInventory
import net.minestom.server.inventory.TransactionOption
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

/**
 * @author Kanelucky
 */
@Command(name = "give")
@Description("Gives an item to a player")
@Permission("minestom4fun.commands.defaults.give")
class GiveCommand {

    @Execute
    fun execute(
        @Context sender: CommandSender,
        @Arg("target") target: Player,
        @Arg("item") item: Material,
        @OptionalArg("count") count: Int?
    ) {
        val actualCount = (count ?: 1).coerceAtMost(PlayerInventory.INVENTORY_SIZE * 64)

        val itemStacks = mutableListOf<ItemStack>()
        var remaining = actualCount
        while (remaining > 64) {
            itemStacks.add(ItemStack.of(item, 64))
            remaining -= 64
        }
        itemStacks.add(ItemStack.of(item, remaining))

        target.inventory.addItemStacks(itemStacks, TransactionOption.ALL)

        val itemName = item.key().value()
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

        sender.sendMessage("Gave ${actualCount} [$itemName] to ${target.username}")
    }
}