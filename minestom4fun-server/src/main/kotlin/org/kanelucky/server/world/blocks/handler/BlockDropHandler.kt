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
                if (event.player.gameMode == GameMode.CREATIVE) {
                    event.isCancelled = true
                    event.player.instance?.setBlock(event.blockPosition, Block.AIR)
                    return@addListener
                }
            }

        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerBlockBreakEvent::class.java) { event ->
                val player = event.player
                val instance = player.instance ?: return@addListener

                if (player.gameMode == GameMode.CREATIVE) return@addListener
                if (event.block !in FragileBlocks.FRAGILE_BLOCKS) return@addListener

                event.isCancelled = true
                instance.setBlock(event.blockPosition, Block.AIR)

                if (hasSilkTouch(player)) return@addListener

                val drop = getDropForBlock(event.block) ?: return@addListener
                val itemEntity = net.minestom.server.entity.ItemEntity(drop)
                itemEntity.setInstance(instance, event.blockPosition.asVec().add(0.5, 0.5, 0.5))
            }
    }

    private fun getDropForBlock(block: Block): net.minestom.server.item.ItemStack? {
        val material = net.minestom.server.item.Material.fromKey(block.key()) ?: return null
        return net.minestom.server.item.ItemStack.of(material)
    }

    private fun hasSilkTouch(player: Player): Boolean {
        val item = player.getItemInMainHand()
        val enchantments = item.get(DataComponents.ENCHANTMENTS) ?: return false
        return enchantments.enchantments().containsKey(Enchantment.SILK_TOUCH)
    }
}