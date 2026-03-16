//package org.kanelucky.world.generator.old.tree
//
//import net.minestom.server.instance.block.Block
//import net.minestom.server.instance.generator.GenerationUnit
//
///**
// * @author Kanelucky
// */
//class OldGeneratorTreeAccess(private val unit: GenerationUnit) {
//
//    companion object {
//        val OAK_LOG       = Block.OAK_LOG
//        val OAK_LOG_X     = Block.OAK_LOG.withProperty("axis", "x")
//        val OAK_LOG_Z     = Block.OAK_LOG.withProperty("axis", "z")
//        val OAK_LEAVES    = Block.OAK_LEAVES.withProperty("persistent", "true")
//        val BIRCH_LOG     = Block.BIRCH_LOG
//        val BIRCH_LEAVES  = Block.BIRCH_LEAVES.withProperty("persistent", "true")
//        val SPRUCE_LOG    = Block.SPRUCE_LOG
//        val SPRUCE_LEAVES = Block.SPRUCE_LEAVES.withProperty("persistent", "true")
//        val JUNGLE_LOG    = Block.JUNGLE_LOG
//        val JUNGLE_LEAVES = Block.JUNGLE_LEAVES.withProperty("persistent", "true")
//        val DIRT          = Block.DIRT
//
//        val VINE_NORTH = Block.VINE.withProperty("north", "true")
//        val VINE_SOUTH = Block.VINE.withProperty("south", "true")
//        val VINE_EAST  = Block.VINE.withProperty("east",  "true")
//        val VINE_WEST  = Block.VINE.withProperty("west",  "true")
//
//        const val GEN_DEPTH = 256
//
//        fun isLeaves(block: Block) = block.name().contains("leaves")
//        fun isLog(block: Block)    = block.name().contains("_log")
//    }
//
//    fun getBlock(x: Int, y: Int, z: Int): Block =
//        unit.getBlock(x, y, z, Block.Getter.Condition.TYPE)
//
//    fun setBlock(x: Int, y: Int, z: Int, block: Block) =
//        unit.modifier().setBlock(x, y, z, block)
//
//    fun isAir(x: Int, y: Int, z: Int)    = getBlock(x, y, z).isAir
//    fun isSolid(x: Int, y: Int, z: Int)  = getBlock(x, y, z).isSolid
//    fun isLeaves(x: Int, y: Int, z: Int) = isLeaves(getBlock(x, y, z))
//    fun isWater(x: Int, y: Int, z: Int)  = getBlock(x, y, z) == Block.WATER
//
//    fun isReplaceableByTree(x: Int, y: Int, z: Int): Boolean {
//        val b = getBlock(x, y, z)
//        return b.isAir || isLeaves(b) || b == Block.WATER
//    }
//
//    fun isGroundForTree(x: Int, y: Int, z: Int): Boolean {
//        val b = getBlock(x, y, z)
//        return b == Block.GRASS_BLOCK || b == Block.DIRT
//    }
//
//    fun getHeightmap(x: Int, z: Int): Int {
//        for (y in GEN_DEPTH - 1 downTo 1) {
//            if (!isAir(x, y, z)) return y + 1
//        }
//        return 0
//    }
//}