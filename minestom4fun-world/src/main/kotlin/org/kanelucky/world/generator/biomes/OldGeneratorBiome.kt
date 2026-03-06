package org.kanelucky.world.generator.biomes

import net.minestom.server.instance.block.Block
import net.minestom.server.registry.RegistryKey
import net.minestom.server.world.biome.Biome
import org.kanelucky.world.generator.blocks.OldGeneratorBlockIds

/**
 * Old biome implementation for legacy world generation
 *
 * Originally developed in OldConsoleWorldgen(https://github.com/zernix2077/OldConsoleWorldgen)
 *
 * Ported to Kotlin by Kanelucky
 */
internal class OldGeneratorBiome private constructor(
    val id: Int,
    private val name: String,
    val topBlock: Block,
    val fillerBlock: Block,
    val depth: Float,
    val scale: Float,
    val temperature: Float,
    val downfall: Float,
    val biomeKey: RegistryKey<Biome>
) {
    val hasSnow: Boolean get() = temperature < 0.15f

    val topBlockId: Int get() = when (topBlock) {
        Block.GRASS_BLOCK -> OldGeneratorBlockIds.GRASS
        Block.SAND        -> OldGeneratorBlockIds.SAND
        Block.MYCELIUM    -> OldGeneratorBlockIds.MYCELIUM
        else              -> OldGeneratorBlockIds.GRASS
    }

    val fillerBlockId: Int get() = when (fillerBlock) {
        Block.SAND -> OldGeneratorBlockIds.SAND
        Block.DIRT -> OldGeneratorBlockIds.DIRT
        else       -> OldGeneratorBlockIds.DIRT
    }

    override fun toString() = name

    companion object {
        private val BIOMES = arrayOfNulls<OldGeneratorBiome>(256)
        private val BY_BIOME = HashMap<RegistryKey<Biome>, OldGeneratorBiome>()

        val OCEAN           = register(0,  "Ocean",                Block.GRASS_BLOCK, Block.DIRT,    -1.0f, 0.4f, 0.5f,  0.5f, Biome.OCEAN)
        val PLAINS          = register(1,  "Plains",               Block.GRASS_BLOCK, Block.DIRT,     0.1f, 0.3f, 0.8f,  0.4f, Biome.PLAINS)
        val DESERT          = register(2,  "Desert",               Block.SAND,        Block.SAND,     0.1f, 0.2f, 2.0f,  0.0f, Biome.DESERT)
        val EXTREME_HILLS   = register(3,  "Extreme Hills",        Block.GRASS_BLOCK, Block.DIRT,     0.3f, 1.5f, 0.2f,  0.3f, Biome.WINDSWEPT_HILLS)
        val FOREST          = register(4,  "Forest",               Block.GRASS_BLOCK, Block.DIRT,     0.1f, 0.3f, 0.7f,  0.8f, Biome.FOREST)
        val TAIGA           = register(5,  "Taiga",                Block.GRASS_BLOCK, Block.DIRT,     0.1f, 0.4f, 0.05f, 0.8f, Biome.TAIGA)
        val SWAMPLAND       = register(6,  "Swampland",            Block.GRASS_BLOCK, Block.DIRT,    -0.2f, 0.1f, 0.8f,  0.9f, Biome.SWAMP)
        val RIVER           = register(7,  "River",                Block.GRASS_BLOCK, Block.DIRT,    -0.5f, 0.0f, 0.5f,  0.5f, Biome.RIVER)
        val HELL            = register(8,  "Hell",                 Block.GRASS_BLOCK, Block.DIRT,     0.1f, 0.3f, 2.0f,  0.0f, Biome.NETHER_WASTES)
        val SKY             = register(9,  "Sky",                  Block.GRASS_BLOCK, Block.DIRT,     0.1f, 0.3f, 0.5f,  0.5f, Biome.THE_END)
        val FROZEN_OCEAN    = register(10, "FrozenOcean",          Block.GRASS_BLOCK, Block.DIRT,    -1.0f, 0.5f, 0.0f,  0.5f, Biome.FROZEN_OCEAN)
        val FROZEN_RIVER    = register(11, "FrozenRiver",          Block.GRASS_BLOCK, Block.DIRT,    -0.5f, 0.0f, 0.0f,  0.5f, Biome.FROZEN_RIVER)
        val ICE_FLATS       = register(12, "Ice Plains",           Block.GRASS_BLOCK, Block.DIRT,     0.1f, 0.3f, 0.0f,  0.5f, Biome.SNOWY_PLAINS)
        val ICE_MOUNTAINS   = register(13, "Ice Mountains",        Block.GRASS_BLOCK, Block.DIRT,     0.3f, 1.3f, 0.0f,  0.5f, Biome.SNOWY_SLOPES)
        val MUSHROOM_ISLAND = register(14, "MushroomIsland",       Block.MYCELIUM,    Block.DIRT,     0.2f, 1.0f, 0.9f,  1.0f, Biome.MUSHROOM_FIELDS)
        val MUSHROOM_ISLAND_SHORE = register(15, "MushroomIslandShore", Block.MYCELIUM, Block.DIRT,  -1.0f, 0.1f, 0.9f,  1.0f, Biome.MUSHROOM_FIELDS)
        val BEACH           = register(16, "Beach",                Block.SAND,        Block.SAND,     0.0f, 0.1f, 0.8f,  0.4f, Biome.BEACH)
        val DESERT_HILLS    = register(17, "DesertHills",          Block.SAND,        Block.SAND,     0.3f, 0.8f, 2.0f,  0.0f, Biome.DESERT)
        val FOREST_HILLS    = register(18, "ForestHills",          Block.GRASS_BLOCK, Block.DIRT,     0.3f, 0.7f, 0.7f,  0.8f, Biome.FOREST)
        val TAIGA_HILLS     = register(19, "TaigaHills",           Block.GRASS_BLOCK, Block.DIRT,     0.3f, 0.8f, 0.05f, 0.8f, Biome.TAIGA)
        val EXTREME_HILLS_EDGE = register(20, "Extreme Hills Edge", Block.GRASS_BLOCK, Block.DIRT,   0.2f, 0.8f, 0.2f,  0.3f, Biome.WINDSWEPT_HILLS)
        val JUNGLE          = register(21, "Jungle",               Block.GRASS_BLOCK, Block.DIRT,     0.2f, 0.4f, 1.2f,  0.9f, Biome.JUNGLE)
        val JUNGLE_HILLS    = register(22, "JungleHills",          Block.GRASS_BLOCK, Block.DIRT,     1.8f, 0.5f, 1.2f,  0.9f, Biome.JUNGLE)

        fun byId(id: Int): OldGeneratorBiome = BIOMES[id and 255] ?: PLAINS
        fun byBiome(key: RegistryKey<Biome>): OldGeneratorBiome = BY_BIOME.getOrDefault(key, PLAINS)

        private fun register(
            id: Int,
            name: String,
            topBlock: Block,
            fillerBlock: Block,
            depth: Float,
            scale: Float,
            temperature: Float,
            downfall: Float,
            biomeKey: RegistryKey<Biome>
        ): OldGeneratorBiome {
            val biome = OldGeneratorBiome(id, name, topBlock, fillerBlock, depth, scale, temperature, downfall, biomeKey)
            BIOMES[id] = biome
            BY_BIOME[biomeKey] = biome
            return biome
        }
    }
}