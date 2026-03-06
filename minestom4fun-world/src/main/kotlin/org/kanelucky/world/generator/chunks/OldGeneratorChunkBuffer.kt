package org.kanelucky.world.generator.chunks

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

import org.kanelucky.world.generator.biomes.OldGeneratorBiome
import org.kanelucky.world.generator.blocks.OldGeneratorBlockIds

internal class OldGeneratorChunkBuffer {
    companion object {
        const val WIDTH = 16
        const val DEPTH = 16
        const val GEN_DEPTH = 128
    }

    private val blocks = IntArray(WIDTH * DEPTH * GEN_DEPTH)
    private val biomes = arrayOfNulls<OldGeneratorBiome>(WIDTH * DEPTH)

    fun getBlock(x: Int, y: Int, z: Int): Int = blocks[index(x, y, z)]
    fun setBlock(x: Int, y: Int, z: Int, blockId: Int) { blocks[index(x, y, z)] = blockId }

    fun getBiome(x: Int, z: Int): OldGeneratorBiome? = biomes[(z shl 4) or x]
    fun setBiome(x: Int, z: Int, biome: OldGeneratorBiome) { biomes[(z shl 4) or x] = biome }

    fun flushToUnit(unit: GenerationUnit, chunkX: Int, chunkZ: Int) {
        val modifier = unit.modifier()
        val baseX = chunkX * 16
        val baseZ = chunkZ * 16

        for (z in 0 until DEPTH) {
            for (x in 0 until WIDTH) {
                for (y in 0 until GEN_DEPTH) {
                    val blockId = getBlock(x, y, z)
                    if (blockId == OldGeneratorBlockIds.AIR) continue
                    modifier.setBlock(baseX + x, y, baseZ + z, mapBlock(blockId))
                }
            }
        }
    }

    private fun index(x: Int, y: Int, z: Int) = (((z shl 4) or x) * GEN_DEPTH) + y

    private fun mapBlock(blockId: Int): Block = when (blockId) {
        OldGeneratorBlockIds.BEDROCK   -> Block.BEDROCK
        OldGeneratorBlockIds.STONE     -> Block.STONE
        OldGeneratorBlockIds.GRASS     -> Block.GRASS_BLOCK
        OldGeneratorBlockIds.DIRT      -> Block.DIRT
        OldGeneratorBlockIds.WATER     -> Block.WATER
        OldGeneratorBlockIds.LAVA      -> Block.LAVA
        OldGeneratorBlockIds.SAND      -> Block.SAND
        OldGeneratorBlockIds.SANDSTONE -> Block.SANDSTONE
        OldGeneratorBlockIds.ICE       -> Block.ICE
        OldGeneratorBlockIds.MYCELIUM  -> Block.MYCELIUM
        else -> Block.AIR
    }
}