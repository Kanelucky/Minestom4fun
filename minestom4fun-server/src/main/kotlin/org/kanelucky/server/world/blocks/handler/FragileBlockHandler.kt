package org.kanelucky.server.world.blocks.handler

import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.instance.block.Block

import org.kanelucky.server.world.blocks.FragileBlocks

/**
 * @author Kanelucky
 */
object FragileBlockHandler {

    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(PlayerBlockBreakEvent::class.java) { event ->
                if (event.isCancelled) return@addListener  // thêm dòng này

                val instance = event.player.instance ?: return@addListener
                val above = event.blockPosition.add(0, 1, 0)
                val blockAbove = instance.getBlock(above)

                if (blockAbove in FragileBlocks.FRAGILE_BLOCKS) {
                    if (blockAbove.getProperty("half") == "lower")
                        instance.setBlock(above.add(0, 1, 0), Block.AIR)
                    instance.setBlock(above, Block.AIR)
                }

                if (event.block.getProperty("half") == "upper")
                    instance.setBlock(event.blockPosition.sub(0, 1, 0), Block.AIR)
            }
    }
}