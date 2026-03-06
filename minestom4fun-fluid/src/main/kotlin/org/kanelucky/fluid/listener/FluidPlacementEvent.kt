package org.kanelucky.fluid.listener

import net.kyori.adventure.key.Key
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EquipmentSlot
import net.minestom.server.entity.PlayerHand
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockFace
import net.minestom.server.instance.block.BlockHandler.PlayerPlacement
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import org.kanelucky.fluid.MinestomFluids

fun setupFluidPlacementEvent() {
    MinecraftServer.getGlobalEventHandler()
        .addListener(PlayerBlockInteractEvent::class.java) { event: PlayerBlockInteractEvent ->
            run {
                if (event.isCancelled) return@run
                val instance = event.player.instance
                val itemInMainHand = event.player.itemInMainHand

                if (itemInMainHand == ItemStack.of(Material.LAVA_BUCKET)) {
                    val blockFace = event.blockFace
                    val block = Block.LAVA
                    val updated = instance.getBlock(event.blockPosition)
                    val placePosition: Pos = event.blockPosition.relative(blockFace).asVec().asPosition()

                    instance.placeBlock(
                        PlayerPlacement(
                            block,
                            updated,
                            instance,
                            placePosition,
                            event.player,
                            PlayerHand.MAIN,
                            blockFace,
                            placePosition.x.toFloat(),
                            placePosition.y.toFloat(),
                            placePosition.z.toFloat()
                        )
                    )
                    if (block != Block.LAVA) {
                        MinestomFluids.scheduleTick(instance, placePosition, Block.LAVA)
                        // Schedule updates for adjacent blocks (ensures fluid spreads properly)
                        for (face in BlockFace.entries) {
                            val neighbor = placePosition.relative(face)
                            val neighborBlock = instance.getBlock(neighbor)
                            if (MinestomFluids.getFluidOnBlock(neighborBlock) !== MinestomFluids.EMPTY) {
                                MinestomFluids.scheduleTick(instance, neighbor, neighborBlock)
                            }
                        }
                    }
                    event.player.refreshPosition(placePosition)
                    event.player.inventory.setEquipment(
                        EquipmentSlot.MAIN_HAND,
                        event.player.heldSlot,
                        ItemStack.of(Material.BUCKET)
                    )
                }
                if (itemInMainHand == ItemStack.of(Material.WATER_BUCKET)) {
                    val blockFace = event.blockFace
                    val block: Block?
                    val updated = instance.getBlock(event.blockPosition)
                    var placePosition: Pos = event.blockPosition.relative(blockFace).asVec().asPosition()

                    if (isWaterloggable(updated)) {
                        block = updated.withProperty("waterlogged", "true")
                        placePosition = event.blockPosition.asVec().asPosition()
                    } else {
                        block = Block.WATER
                    }
                    instance.placeBlock(
                        PlayerPlacement(
                            block,
                            updated,
                            instance,
                            placePosition,
                            event.player,
                            PlayerHand.MAIN,
                            blockFace,
                            placePosition.x.toFloat(),
                            placePosition.y.toFloat(),
                            placePosition.z.toFloat()
                        )
                    )
                    if (block != Block.WATER) {
                        // Schedule update for the current block
                        MinestomFluids.scheduleTick(instance, placePosition, Block.WATER)

                        // Schedule updates for adjacent blocks (ensures fluid spreads properly)
                        for (face in BlockFace.entries) {
                            val neighbor = placePosition.relative(face)
                            val neighborBlock = instance.getBlock(neighbor)
                            if (MinestomFluids.getFluidOnBlock(neighborBlock) !== MinestomFluids.EMPTY) {
                                MinestomFluids.scheduleTick(instance, neighbor, neighborBlock)
                            }
                        }
                    }
                    event.player.refreshPosition(placePosition, false, true)
                    event.player.inventory.setEquipment(
                        EquipmentSlot.MAIN_HAND,
                        event.player.heldSlot,
                        ItemStack.of(Material.BUCKET)
                    )
                }
            }
        }
}

fun isWaterloggable(block: Block): Boolean {
    val tags = Block.staticRegistry()
    if (tags.getTag(Key.key("minecraft:stairs"))!!.contains(block)
        || tags.getTag(Key.key("minecraft:slabs"))!!.contains(block)
        || tags.getTag(Key.key("minecraft:fences"))!!.contains(block)
        || tags.getTag(Key.key("minecraft:trapdoors"))!!.contains(block)

    ) {
        return true
    }
    return false
}