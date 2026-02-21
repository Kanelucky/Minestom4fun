package org.kanelucky.server.world.blocks.handler

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.ItemEntity
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

import org.kanelucky.server.world.rates.DropRates.SEED_DROP_CHANCE

import kotlin.random.Random

/**
 * @author Kanelucky
 */
object GrassDropHandler {

    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerBlockBreakEvent::class.java) { event ->
                val instance = event.player.instance ?: return@addListener

                if (event.block != Block.SHORT_GRASS && event.block != Block.TALL_GRASS)
                    return@addListener

                if (Random.Default.nextFloat() > SEED_DROP_CHANCE) return@addListener

                val pos = Pos(
                    event.blockPosition.x() + 0.5,
                    event.blockPosition.y().toDouble(),
                    event.blockPosition.z() + 0.5
                )

                val seed = ItemEntity(ItemStack.of(Material.WHEAT_SEEDS))
                seed.setInstance(instance, pos)
            }
    }
}