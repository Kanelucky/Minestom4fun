package org.kanelucky.world.generator.features.objects

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Vanilla-accurate tree generation ported from OldOverworldPopulator.
 *
 * @author Kanelucky
 */
object TreeGenerator {

    // --- Blocks ---
    private val OAK_LOG      = Block.OAK_LOG
    private val OAK_LOG_X    = Block.OAK_LOG.withProperty("axis", "x")
    private val OAK_LOG_Z    = Block.OAK_LOG.withProperty("axis", "z")
    private val OAK_LEAVES   = Block.OAK_LEAVES.withProperty("persistent", "true")
    private val BIRCH_LOG    = Block.BIRCH_LOG
    private val BIRCH_LEAVES = Block.BIRCH_LEAVES.withProperty("persistent", "true")
    private val SPRUCE_LOG   = Block.SPRUCE_LOG
    private val SPRUCE_LEAVES= Block.SPRUCE_LEAVES.withProperty("persistent", "true")
    private val VINE_N = Block.VINE.withProperty("north", "true")
    private val VINE_S = Block.VINE.withProperty("south", "true")
    private val VINE_E = Block.VINE.withProperty("east",  "true")
    private val VINE_W = Block.VINE.withProperty("west",  "true")

    private val AXIS_CONVERSION = byteArrayOf(2, 0, 0, 1, 2, 1)

    // --- Public API ---

    fun generateOak(unit: GenerationUnit, x: Int, y: Int, z: Int, random: Random) {
        if (random.nextInt(10) == 0) generateFancyOak(unit, x, y, z, random)
        else generateBasicOak(unit, x, y, z, random)
    }

    fun generateBirch(unit: GenerationUnit, x: Int, y: Int, z: Int, random: Random) =
        placeBirch(unit, x, y, z, random)

    fun generateSpruce(unit: GenerationUnit, x: Int, y: Int, z: Int, random: Random) {
        if (random.nextInt(3) == 0) placePine(unit, x, y, z, random)
        else placeSpruce(unit, x, y, z, random)
    }

    // --- Oak (basic) ---

    private fun generateBasicOak(unit: GenerationUnit, x: Int, y: Int, z: Int, random: Random) {
        val height = random.nextInt(3) + 4
        if (!checkGround(unit, x, y - 1, z)) return
        replaceWithDirt(unit, x, y - 1, z)

        // leaves
        for (yy in y + height downTo y - 3 + height) {
            val yo = yy - (y + height)
            val offs = 1 - yo / 2
            for (xx in x - offs..x + offs) {
                val xo = xx - x
                for (zz in z - offs..z + offs) {
                    val zo = zz - z
                    if (abs(xo) == offs && abs(zo) == offs && (random.nextInt(2) == 0 || yo == 0)) continue
                    placeLeaf(unit, xx, yy, zz, OAK_LEAVES)
                }
            }
        }
        // trunk
        for (hh in 0 until height) placeLog(unit, x, y + hh, z, OAK_LOG)
    }

    // --- Oak (fancy) ---

    private fun generateFancyOak(unit: GenerationUnit, x: Int, y: Int, z: Int, random: Random) {
        val height = 5 + random.nextInt(12)
        val trunkHeight = minOf((height * 0.618).toInt(), height - 1)
        val foliageHeight = 4

        if (!checkGround(unit, x, y - 1, z)) return

        var clustersPerY = maxOf(1, (1.382 + Math.pow(height / 13.0, 2.0)).toInt())
        val coords = mutableListOf<IntArray>()
        val trunkTop = y + trunkHeight
        var cy = y + height - foliageHeight
        var relY = cy - y

        coords.add(intArrayOf(cy, x, cy, z, trunkTop))

        while (relY >= 0) {
            val shapeFactor = fancyShape(relY, height)
            if (shapeFactor >= 0f) {
                repeat(clustersPerY) {
                    val radius = shapeFactor * (random.nextFloat() + 0.328f)
                    val angle = random.nextFloat() * 2.0 * Math.PI
                    val bx = floor(radius * Math.sin(angle) + x + 0.5)
                    val bz = floor(radius * Math.cos(angle) + z + 0.5)
                    val dist = Math.sqrt(Math.pow(abs(x - bx).toDouble(), 2.0) + Math.pow(abs(z - bz).toDouble(), 2.0))
                    val baseY = if ((cy - dist * 0.381) > trunkTop) trunkTop else (cy - dist * 0.381).toInt()
                    coords.add(intArrayOf(bx, cy, bz, baseY))
                }
            }
            cy--; relY--
        }

        // foliage clusters
        for (c in coords) foliageCluster(unit, c[0], c[1], c[2])

        // trunk
        limb(unit, intArrayOf(x, y, z), intArrayOf(x, y + trunkHeight, z), false)

        // branches
        for (c in coords) {
            val base = intArrayOf(x, c[3], z)
            if (base[1] - y >= height * 0.2) {
                limb(unit, base, intArrayOf(c[0], c[1], c[2]), false)
            }
        }
    }

    private fun fancyShape(y: Int, height: Int): Float {
        if (y < height * 0.3f) return -1.618f
        val r = height / 2f
        val adj = r - y
        return when {
            adj == 0f        -> r
            abs(adj) >= r    -> 0f
            else             -> sqrt(r * r - adj * adj)
        } * 0.5f
    }

    private fun foliageCluster(unit: GenerationUnit, x: Int, y: Int, z: Int) {
        val foliageHeight = 4
        for (yy in y + foliageHeight - 1 downTo y) {
            val radius = when {
                yy - y == 0 || yy - y == foliageHeight - 1 -> 2f
                yy < y || yy >= y + foliageHeight           -> -1f
                else                                        -> 3f
            }
            if (radius < 0) continue
            val rad = (radius + 0.618).toInt()
            for (ox in -rad..rad) {
                for (oz in -rad..rad) {
                    val dist = Math.pow(abs(ox) + 0.5, 2.0) + Math.pow(abs(oz) + 0.5, 2.0)
                    if (dist > radius * radius) continue
                    placeLeaf(unit, x + ox, yy, z + oz, OAK_LEAVES)
                }
            }
        }
    }

    private fun limb(unit: GenerationUnit, start: IntArray, end: IntArray, leaves: Boolean) {
        val delta = IntArray(3) { end[it] - start[it] }
        var primary = 0
        for (i in 1..2) if (abs(delta[i]) > abs(delta[primary])) primary = i
        if (delta[primary] == 0) return

        val s1 = AXIS_CONVERSION[primary].toInt()
        val s2 = AXIS_CONVERSION[primary + 3].toInt()
        val sign = if (delta[primary] > 0) 1 else -1
        val f1 = delta[s1].toDouble() / delta[primary]
        val f2 = delta[s2].toDouble() / delta[primary]
        val coord = IntArray(3)

        var offset = 0
        val endOffset = delta[primary] + sign
        while (offset != endOffset) {
            coord[primary] = floor(start[primary] + offset + 0.5)
            coord[s1] = floor(start[s1] + offset * f1 + 0.5)
            coord[s2] = floor(start[s2] + offset * f2 + 0.5)
            if (leaves) placeLeaf(unit, coord[0], coord[1], coord[2], OAK_LEAVES)
            else {
                val xd = abs(coord[0] - start[0])
                val zd = abs(coord[2] - start[2])
                val log = when {
                    xd > zd -> OAK_LOG_X
                    zd > xd -> OAK_LOG_Z
                    else    -> OAK_LOG
                }
                placeLog(unit, coord[0], coord[1], coord[2], log)
            }
            offset += sign
        }
    }

    // --- Birch ---

    private fun placeBirch(unit: GenerationUnit, x: Int, y: Int, z: Int, random: Random) {
        val height = random.nextInt(3) + 5
        if (!checkGround(unit, x, y - 1, z)) return
        replaceWithDirt(unit, x, y - 1, z)

        for (yy in y - 3 + height..y + height) {
            val yo = yy - (y + height)
            val offs = 1 - yo / 2
            for (xx in x - offs..x + offs) {
                val xo = xx - x
                for (zz in z - offs..z + offs) {
                    val zo = zz - z
                    if (abs(xo) == offs && abs(zo) == offs && (random.nextInt(2) == 0 || yo == 0)) continue
                    placeLeaf(unit, xx, yy, zz, BIRCH_LEAVES)
                }
            }
        }
        for (hh in 0 until height) placeLog(unit, x, y + hh, z, BIRCH_LOG)
    }

    // --- Spruce ---

    private fun placeSpruce(unit: GenerationUnit, x: Int, y: Int, z: Int, random: Random) {
        val height = random.nextInt(4) + 6
        val trunkHeight = 1 + random.nextInt(2)
        val leafRadius = 2 + random.nextInt(2)
        if (!checkGround(unit, x, y - 1, z)) return
        replaceWithDirt(unit, x, y - 1, z)

        val topHeight = height - trunkHeight
        var currentRadius = random.nextInt(2)
        var maxRadius = 1
        var minRadius = 0

        for (pos in 0..topHeight) {
            val yy = y + height - pos
            for (xx in x - currentRadius..x + currentRadius) {
                val xo = xx - x
                for (zz in z - currentRadius..z + currentRadius) {
                    val zo = zz - z
                    if (abs(xo) == currentRadius && abs(zo) == currentRadius && currentRadius > 0) continue
                    placeLeaf(unit, xx, yy, zz, SPRUCE_LEAVES)
                }
            }
            if (currentRadius >= maxRadius) {
                currentRadius = minRadius; minRadius = 1
                maxRadius = minOf(maxRadius + 1, leafRadius)
            } else currentRadius++
        }

        val topOffset = random.nextInt(3)
        for (hh in 0 until height - topOffset) placeLog(unit, x, y + hh, z, SPRUCE_LOG)
    }

    // --- Pine ---

    private fun placePine(unit: GenerationUnit, x: Int, y: Int, z: Int, random: Random) {
        val height = random.nextInt(5) + 7
        val trunkHeight = height - random.nextInt(2) - 3
        val topRadius = 1 + random.nextInt(height - trunkHeight + 1)
        if (!checkGround(unit, x, y - 1, z)) return
        replaceWithDirt(unit, x, y - 1, z)

        var currentRadius = 0
        for (yy in y + height downTo y + trunkHeight) {
            for (xx in x - currentRadius..x + currentRadius) {
                val xo = xx - x
                for (zz in z - currentRadius..z + currentRadius) {
                    val zo = zz - z
                    if (abs(xo) == currentRadius && abs(zo) == currentRadius && currentRadius > 0) continue
                    placeLeaf(unit, xx, yy, zz, SPRUCE_LEAVES)
                }
            }
            if (currentRadius >= 1 && yy == y + trunkHeight + 1) currentRadius--
            else if (currentRadius < topRadius) currentRadius++
        }

        for (hh in 0 until height - 1) placeLog(unit, x, y + hh, z, SPRUCE_LOG)
    }

    // --- Helpers ---

    private fun checkGround(unit: GenerationUnit, x: Int, y: Int, z: Int): Boolean {
        return true
    }

    private fun replaceWithDirt(unit: GenerationUnit, x: Int, y: Int, z: Int) =
        safeSet(unit, x, y, z, Block.DIRT)

    private fun placeLeaf(unit: GenerationUnit, x: Int, y: Int, z: Int, block: Block) {
        safeSet(unit, x, y, z, block)
    }

    private fun placeLog(unit: GenerationUnit, x: Int, y: Int, z: Int, block: Block) {
        safeSet(unit, x, y, z, block)
    }

    private fun addVine(unit: GenerationUnit, x: Int, y: Int, z: Int, vine: Block) {
        safeSet(unit, x, y, z, vine)
        for (i in 1..4) safeSet(unit, x, y - i, z, vine)
    }

    private fun safeSet(unit: GenerationUnit, x: Int, y: Int, z: Int, block: Block) {
        val s = unit.absoluteStart(); val e = unit.absoluteEnd()
        if (x in s.blockX() until e.blockX() &&
            y in s.blockY() until e.blockY() &&
            z in s.blockZ() until e.blockZ())
            unit.modifier().setBlock(x, y, z, block)
    }

    private fun floor(v: Double): Int { val f = v.toInt(); return if (v < f) f - 1 else f }
}