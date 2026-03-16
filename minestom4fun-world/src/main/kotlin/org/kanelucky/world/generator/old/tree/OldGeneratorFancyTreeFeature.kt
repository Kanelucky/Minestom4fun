//package org.kanelucky.world.generator.old.tree
//
//import net.minestom.server.instance.block.Block
//import kotlin.math.*
//
///**
// * @author Kanelucky
// */
//class OldGeneratorFancyTreeFeature : OldGeneratorAbstractTreeFeature() {
//
//    companion object {
//        private val AXIS_CONVERSION = byteArrayOf(2, 0, 0, 1, 2, 1)
//    }
//
//    private var height = 0
//    private var trunkHeight = 0
//    private val trunkHeightScale = 0.618
//    private val branchDensity = 1.0
//    private val branchSlope = 0.381
//    private val widthScale = 1.0
//    private val foliageDensity = 1.0
//    private val foliageHeight = 4
//    private var foliageCoords: List<IntArray> = emptyList()
//    private var origin = IntArray(3)
//
//    override fun place(world: OldGeneratorTreeAccess, random: OldGeneratorNoise, x: Int, y: Int, z: Int): Boolean {
//        val innerRandom = OldGeneratorNoise(random.nextLong())
//        origin = intArrayOf(x, y, z)
//        height = 5 + innerRandom.nextInt(12)
//        foliageCoords = emptyList()
//
//        if (!checkLocation(world)) return false
//
//        prepare(innerRandom)
//        makeFoliage(world)
//        makeTrunk(world)
//        makeBranches(world)
//        return true
//    }
//
//    private fun prepare(random: OldGeneratorNoise) {
//        trunkHeight = (height * trunkHeightScale).toInt()
//        if (trunkHeight >= height) trunkHeight = height - 1
//
//        var clustersPerY = (1.382 + (foliageDensity * height / 13.0).pow(2.0)).toInt()
//        if (clustersPerY < 1) clustersPerY = 1
//
//        val coords = mutableListOf<IntArray>()
//        var y = origin[1] + height - foliageHeight
//        val trunkTop = origin[1] + trunkHeight
//        var relativeY = y - origin[1]
//
//        coords.add(intArrayOf(origin[0], y, origin[2], trunkTop))
//        y--
//
//        while (relativeY >= 0) {
//            val shapeFactor = treeShape(relativeY)
//            if (shapeFactor < 0f) { y--; relativeY--; continue }
//
//            repeat(clustersPerY) {
//                val radius = widthScale * (shapeFactor * (random.nextFloat() + 0.328))
//                val angle = random.nextFloat() * 2.0 * Math.PI
//                val cx = floor(radius * sin(angle) + origin[0] + 0.5)
//                val cz = floor(radius * cos(angle) + origin[2] + 0.5)
//                val checkStart = intArrayOf(cx, y, cz)
//                val checkEnd = intArrayOf(cx, y + foliageHeight, cz)
//                // simplified: always add the coord (full line check needs world access)
//                val dist = sqrt((abs(origin[0] - cx)).toDouble().pow(2.0) + (abs(origin[2] - cz)).toDouble().pow(2.0))
//                val branchBaseY = if ((checkStart[1] - dist * branchSlope) > trunkTop) trunkTop
//                else (checkStart[1] - dist * branchSlope).toInt()
//                coords.add(intArrayOf(cx, y, cz, branchBaseY))
//            }
//            y--; relativeY--
//        }
//        foliageCoords = coords
//    }
//
//    private fun treeShape(y: Int): Float {
//        if (y < height * 0.3f) return -1.618f
//        val radius = height / 2f
//        val adjacent = height / 2f - y
//        return when {
//            adjacent == 0f -> radius
//            abs(adjacent) >= radius -> 0f
//            else -> sqrt(radius.pow(2) - abs(adjacent).pow(2))
//        } * 0.5f
//    }
//
//    private fun foliageShape(y: Int): Float = when {
//        y < 0 || y >= foliageHeight -> -1f
//        y == 0 || y == foliageHeight - 1 -> 2f
//        else -> 3f
//    }
//
//    private fun foliageCluster(world: OldGeneratorTreeAccess, x: Int, y: Int, z: Int) {
//        for (currentY in y + foliageHeight - 1 downTo y) {
//            val radius = foliageShape(currentY - y)
//            crossSection(world, x, currentY, z, radius, 1, OldGeneratorTreeAccess.OAK_LEAVES)
//        }
//    }
//
//    private fun crossSection(world: OldGeneratorTreeAccess, x: Int, y: Int, z: Int, radius: Float, direction: Int, block: Block) {
//        val rad = (radius + 0.618).toInt()
//        val secIdx1 = AXIS_CONVERSION[direction].toInt()
//        val secIdx2 = AXIS_CONVERSION[direction + 3].toInt()
//        val center = intArrayOf(x, y, z)
//        val pos = intArrayOf(0, 0, 0)
//        pos[direction] = center[direction]
//        for (o1 in -rad..rad) {
//            pos[secIdx1] = center[secIdx1] + o1
//            for (o2 in -rad..rad) {
//                val dist = (abs(o1) + 0.5).pow(2.0) + (abs(o2) + 0.5).pow(2.0)
//                if (dist > radius * radius) continue
//                pos[secIdx2] = center[secIdx2] + o2
//                val current = world.getBlock(pos[0], pos[1], pos[2])
//                if (!current.isAir && !OldGeneratorTreeAccess.isLeaves(current)) continue
//                world.setBlock(pos[0], pos[1], pos[2], block)
//            }
//        }
//    }
//
//    private fun limb(world: OldGeneratorTreeAccess, start: IntArray, end: IntArray, leaves: Boolean) {
//        val delta = IntArray(3) { end[it] - start[it] }
//        var primaryIndex = 0
//        for (i in 1..2) if (abs(delta[i]) > abs(delta[primaryIndex])) primaryIndex = i
//        if (delta[primaryIndex] == 0) return
//
//        val secIdx1 = AXIS_CONVERSION[primaryIndex].toInt()
//        val secIdx2 = AXIS_CONVERSION[primaryIndex + 3].toInt()
//        val sign = if (delta[primaryIndex] > 0) 1 else -1
//        val f1 = delta[secIdx1].toDouble() / delta[primaryIndex]
//        val f2 = delta[secIdx2].toDouble() / delta[primaryIndex]
//        val coord = IntArray(3)
//        var offset = 0
//        val endOffset = delta[primaryIndex] + sign
//        while (offset != endOffset) {
//            coord[primaryIndex] = floor(start[primaryIndex] + offset + 0.5)
//            coord[secIdx1] = floor(start[secIdx1] + offset * f1 + 0.5)
//            coord[secIdx2] = floor(start[secIdx2] + offset * f2 + 0.5)
//            if (leaves) world.setBlock(coord[0], coord[1], coord[2], OldGeneratorTreeAccess.OAK_LEAVES)
//            else world.setBlock(coord[0], coord[1], coord[2], oakLogForAxis(coord, start))
//            offset += sign
//        }
//    }
//
//    private fun makeFoliage(world: OldGeneratorTreeAccess) =
//        foliageCoords.forEach { foliageCluster(world, it[0], it[1], it[2]) }
//
//    private fun makeTrunk(world: OldGeneratorTreeAccess) =
//        limb(world, intArrayOf(origin[0], origin[1], origin[2]), intArrayOf(origin[0], origin[1] + trunkHeight, origin[2]), false)
//
//    private fun makeBranches(world: OldGeneratorTreeAccess) {
//        val base = intArrayOf(origin[0], origin[1], origin[2])
//        for (coord in foliageCoords) {
//            base[1] = coord[3]
//            if (base[1] - origin[1] >= height * 0.2) {
//                limb(world, base, intArrayOf(coord[0], coord[1], coord[2]), false)
//            }
//        }
//    }
//
//    private fun checkLocation(world: OldGeneratorTreeAccess): Boolean {
//        val base = world.getBlock(origin[0], origin[1] - 1, origin[2])
//        return base == Block.GRASS_BLOCK || base == Block.DIRT
//    }
//
//    private fun oakLogForAxis(coordinate: IntArray, start: IntArray): Block {
//        val xDiff = abs(coordinate[0] - start[0])
//        val zDiff = abs(coordinate[2] - start[2])
//        return when {
//            xDiff > zDiff -> OldGeneratorTreeAccess.OAK_LOG_X
//            zDiff > xDiff -> OldGeneratorTreeAccess.OAK_LOG_Z
//            else -> OldGeneratorTreeAccess.OAK_LOG
//        }
//    }
//
//    private fun floor(v: Double): Int { val f = v.toInt(); return if (v < f) f - 1 else f }
//}