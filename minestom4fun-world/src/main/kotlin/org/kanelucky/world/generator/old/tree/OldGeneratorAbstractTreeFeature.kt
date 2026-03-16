//package org.kanelucky.world.generator.old.tree
//
//import net.minestom.server.instance.block.Block
//import org.kanelucky.world.generator.noise.OldGeneratorNoise
//
///**
// * @author Kanelucky
// */
//abstract class OldGeneratorAbstractTreeFeature {
//
//    abstract fun place(world: OldGeneratorTreeAccess, random: OldGeneratorNoise, x: Int, y: Int, z: Int): Boolean
//
//    protected fun replaceWithDirt(world: OldGeneratorTreeAccess, x: Int, y: Int, z: Int) {
//        world.setBlock(x, y, z, OldGeneratorTreeAccess.DIRT)
//    }
//
//    protected fun placeLeaf(world: OldGeneratorTreeAccess, x: Int, y: Int, z: Int, leaves: Block) {
//        if (!world.isSolid(x, y, z)) world.setBlock(x, y, z, leaves)
//    }
//
//    protected fun placeLog(world: OldGeneratorTreeAccess, x: Int, y: Int, z: Int, log: Block) {
//        val b = world.getBlock(x, y, z)
//        if (b.isAir || OldGeneratorTreeAccess.isLeaves(b) || world.isWater(x, y, z)) {
//            world.setBlock(x, y, z, log)
//        }
//    }
//
//    protected fun addVine(world: OldGeneratorTreeAccess, x: Int, y: Int, z: Int, vine: Block) {
//        if (!world.isAir(x, y, z)) return
//        world.setBlock(x, y, z, vine)
//        var remaining = 4
//        var yy = y - 1
//        while (remaining-- > 0 && world.isAir(x, yy, z)) {
//            world.setBlock(x, yy, z, vine)
//            yy--
//        }
//    }
//}