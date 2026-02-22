package org.kanelucky.server.world.fluid

import io.github.togar2.fluids.FluidState
import io.github.togar2.fluids.MinestomFluids

import net.minestom.server.entity.GameMode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

/**
 * @author Kanelucky
 */
object FluidEventHandler {

    fun register(handler: GlobalEventHandler) {
        handler.addListener(PlayerBlockInteractEvent::class.java) { event ->
            val creative = event.player.gameMode == GameMode.CREATIVE
            val hand = event.hand
            val item = event.player.getItemInHand(hand)

            when (item.material()) {
                Material.WATER_BUCKET -> {
                    event.isBlockingItemUse = true
                    val placePos = event.blockPosition.relative(event.blockFace)
                    val waterlogHandler = MinestomFluids.getWaterlog(event.block)
                    if (waterlogHandler != null) {
                        waterlogHandler.placeFluid(event.instance, event.blockPosition, MinestomFluids.WATER.defaultState)
                    } else {
                        event.instance.setBlock(placePos, Block.WATER)
                        MinestomFluids.scheduleTick(event.instance, placePos, MinestomFluids.WATER.defaultState)
                    }
                    if (!creative) event.player.setItemInHand(hand, ItemStack.of(Material.BUCKET))
                }
                Material.LAVA_BUCKET -> {
                    event.isBlockingItemUse = true
                    val placePos = event.blockPosition.relative(event.blockFace)
                    event.instance.setBlock(placePos, Block.LAVA)
                    MinestomFluids.scheduleTick(event.instance, placePos, MinestomFluids.LAVA.defaultState)
                    if (!creative) event.player.setItemInHand(hand, ItemStack.of(Material.BUCKET))
                }
                Material.BUCKET -> {
                    event.isBlockingItemUse = true
                    val waterlogHandler = MinestomFluids.getWaterlog(event.block)
                    if (waterlogHandler != null && waterlogHandler.canRemoveFluid(event.instance, event.blockPosition, FluidState.of(event.block))) {
                        event.instance.setBlock(event.blockPosition, FluidState.setWaterlogged(event.block, false))
                        if (!creative) event.player.setItemInHand(hand, ItemStack.of(Material.WATER_BUCKET))
                    } else if (event.block.isLiquid) {
                        val state = FluidState.of(event.block)
                        if (!creative) event.player.setItemInHand(hand, state.fluid().bucket)
                        event.instance.setBlock(event.blockPosition, Block.AIR)
                    }
                }
                else -> {}
            }
        }
    }
}