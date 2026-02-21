package org.kanelucky.server.world.blocks.handler

import net.minestom.server.MinecraftServer
import net.minestom.server.component.DataComponents
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.item.enchant.Enchantment

import org.kanelucky.server.world.blocks.FragileBlocks

/**
 * @author Kanelucky
 */
object BlockDropHandler {

    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerBlockBreakEvent::class.java) { event ->
                val player = event.player
                val instance = player.instance ?: return@addListener

                if (event.block !in FragileBlocks.FRAGILE_BLOCKS) return@addListener

                event.isCancelled = true
                instance.setBlock(event.blockPosition, Block.AIR)

                if (player.gameMode == GameMode.CREATIVE) return@addListener
                if (hasSilkTouch(player)) return@addListener
            }
    }

    private fun hasSilkTouch(player: Player): Boolean {
        val item = player.getItemInMainHand()
        val enchantments = item.get(DataComponents.ENCHANTMENTS) ?: return false
        return enchantments.enchantments().containsKey(Enchantment.SILK_TOUCH)
    }
}