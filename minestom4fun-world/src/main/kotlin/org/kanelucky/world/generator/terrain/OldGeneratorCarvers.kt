package org.kanelucky.world.generator.carver

import org.kanelucky.world.generator.blocks.OldGeneratorBlockIds
import org.kanelucky.world.generator.chunks.OldGeneratorChunkBuffer
import org.kanelucky.world.generator.noise.OldGeneratorNoise
import kotlin.math.*

/**
 * Originally developed in OldConsoleWorldgen (https://github.com/zernix2077/OldConsoleWorldgen)
 *
 * Ported to Kotlin/Minestom by Kanelucky
 */
internal abstract class OldGeneratorCarver {
    protected val radius = 8
    protected val random = OldGeneratorNoise(0L)
    protected var seed: Long = 0

    fun apply(seed: Long, chunkX: Int, chunkZ: Int, buffer: OldGeneratorChunkBuffer) {
        this.seed = seed
        random.setSeed(seed)
        val xScale = random.nextLong()
        val zScale = random.nextLong()
        for (x in chunkX - radius..chunkX + radius) {
            for (z in chunkZ - radius..chunkZ + radius) {
                random.setSeed((x * xScale) xor (z * zScale) xor seed)
                addFeature(x, z, chunkX, chunkZ, buffer)
            }
        }
    }

    protected abstract fun addFeature(startChunkX: Int, startChunkZ: Int, targetChunkX: Int, targetChunkZ: Int, buffer: OldGeneratorChunkBuffer)
}

internal class OldGeneratorCaveCarver : OldGeneratorCarver() {

    override fun addFeature(startChunkX: Int, startChunkZ: Int, targetChunkX: Int, targetChunkZ: Int, buffer: OldGeneratorChunkBuffer) {
        var caves = random.nextInt(random.nextInt(random.nextInt(40) + 1) + 1)
        if (random.nextInt(15) != 0) caves = 0

        for (cave in 0 until caves) {
            val xCave = startChunkX * 16.0 + random.nextInt(16)
            val yCave = random.nextInt(random.nextInt(OldGeneratorChunkBuffer.GEN_DEPTH - 8) + 8).toDouble()
            val zCave = startChunkZ * 16.0 + random.nextInt(16)

            var tunnels = 1
            if (random.nextInt(4) == 0) {
                addRoom(random.nextLong(), targetChunkX, targetChunkZ, buffer, xCave, yCave, zCave)
                tunnels += random.nextInt(4)
            }

            for (i in 0 until tunnels) {
                val yRot = random.nextFloat() * PI.toFloat() * 2.0f
                val xRot = ((random.nextFloat() - 0.5f) * 2.0f) / 8.0f
                var thickness = random.nextFloat() * 2.0f + random.nextFloat()
                if (random.nextInt(10) == 0) {
                    thickness *= random.nextFloat() * random.nextFloat() * 3.0f + 1.0f
                }
                addTunnel(random.nextLong(), targetChunkX, targetChunkZ, buffer, xCave, yCave, zCave, thickness, yRot, xRot, 0, 0, 1.0)
            }
        }
    }

    private fun addRoom(seed: Long, chunkX: Int, chunkZ: Int, buffer: OldGeneratorChunkBuffer, xRoom: Double, yRoom: Double, zRoom: Double) {
        addTunnel(seed, chunkX, chunkZ, buffer, xRoom, yRoom, zRoom, 1.0f + random.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5)
    }

    private fun addTunnel(
        tunnelSeed: Long, chunkX: Int, chunkZ: Int, buffer: OldGeneratorChunkBuffer,
        xCaveIn: Double, yCaveIn: Double, zCaveIn: Double,
        thickness: Float, yRotIn: Float, xRotIn: Float,
        stepIn: Int, distIn: Int, yScale: Double
    ) {
        val xMid = chunkX * 16.0 + 8.0
        val zMid = chunkZ * 16.0 + 8.0
        var yRota = 0.0f
        var xRota = 0.0f
        val tunnelRandom = OldGeneratorNoise(tunnelSeed)

        var dist = distIn
        if (dist <= 0) {
            val max = radius * 16 - 16
            dist = max - tunnelRandom.nextInt(max / 4)
        }

        var singleStep = false
        var step = stepIn
        if (step == -1) {
            step = dist / 2
            singleStep = true
        }

        val splitPoint = tunnelRandom.nextInt(dist / 2) + dist / 4
        val steep = tunnelRandom.nextInt(6) == 0

        var xCave = xCaveIn; var yCave = yCaveIn; var zCave = zCaveIn
        var yRot = yRotIn; var xRot = xRotIn

        while (step < dist) {
            val rad = 1.5 + sin(step * PI / dist) * thickness
            val yRad = rad * yScale

            val xc = cos(xRot.toDouble()).toFloat()
            val xs = sin(xRot.toDouble()).toFloat()
            xCave += cos(yRot.toDouble()) * xc
            yCave += xs
            zCave += sin(yRot.toDouble()) * xc

            xRot *= if (steep) 0.92f else 0.7f
            xRot += xRota * 0.1f
            yRot += yRota * 0.1f
            xRota *= 0.90f
            yRota *= 0.75f
            xRota += (tunnelRandom.nextFloat() - tunnelRandom.nextFloat()) * tunnelRandom.nextFloat() * 2.0f
            yRota += (tunnelRandom.nextFloat() - tunnelRandom.nextFloat()) * tunnelRandom.nextFloat() * 4.0f

            if (!singleStep && step == splitPoint && thickness > 1.0f && dist > 0) {
                addTunnel(tunnelRandom.nextLong(), chunkX, chunkZ, buffer, xCave, yCave, zCave,
                    tunnelRandom.nextFloat() * 0.5f + 0.5f, yRot - PI.toFloat() / 2.0f, xRot / 3.0f, step, dist, 1.0)
                addTunnel(tunnelRandom.nextLong(), chunkX, chunkZ, buffer, xCave, yCave, zCave,
                    tunnelRandom.nextFloat() * 0.5f + 0.5f, yRot + PI.toFloat() / 2.0f, xRot / 3.0f, step, dist, 1.0)
                return
            }

            if (!singleStep && tunnelRandom.nextInt(4) == 0) { step++; continue }

            val xd = xCave - xMid
            val zd = zCave - zMid
            val remaining = dist - step
            val rr = thickness + 18.0
            if (xd * xd + zd * zd - remaining * remaining > rr * rr) return

            if (xCave < xMid - 16.0 - rad * 2.0 || zCave < zMid - 16.0 - rad * 2.0
                || xCave > xMid + 16.0 + rad * 2.0 || zCave > zMid + 16.0 + rad * 2.0) { step++; continue }

            val x0 = max(0, floor(xCave - rad) - chunkX * 16 - 1)
            val x1 = min(16, floor(xCave + rad) - chunkX * 16 + 1)
            val y0 = max(1, floor(yCave - yRad) - 1)
            val y1 = min(OldGeneratorChunkBuffer.GEN_DEPTH - 8, floor(yCave + yRad) + 1)
            val z0 = max(0, floor(zCave - rad) - chunkZ * 16 - 1)
            val z1 = min(16, floor(zCave + rad) - chunkZ * 16 + 1)

            if (detectWater(buffer, x0, x1, y0, y1, z0, z1)) { step++; continue }

            carveEllipsoid(buffer, chunkX, chunkZ, xCave, yCave, zCave, rad, yRad, x0, x1, y0, y1, z0, z1, false, null)
            if (singleStep) break
            step++
        }
    }

    companion object {
        fun carveEllipsoid(
            buffer: OldGeneratorChunkBuffer, chunkX: Int, chunkZ: Int,
            xCave: Double, yCave: Double, zCave: Double,
            rad: Double, yRad: Double,
            x0: Int, x1: Int, y0: Int, y1: Int, z0: Int, z1: Int,
            canyon: Boolean, rs: FloatArray?
        ) {
            for (xx in x0 until x1) {
                val xd = ((xx + chunkX * 16 + 0.5) - xCave) / rad
                for (zz in z0 until z1) {
                    val zd = ((zz + chunkZ * 16 + 0.5) - zCave) / rad
                    var hasGrass = false
                    if (xd * xd + zd * zd >= 1.0) continue
                    for (yy in y1 - 1 downTo y0) {
                        val yd = (yy + 0.5 - yCave) / yRad
                        val test = if (canyon) (xd * xd + zd * zd) * rs!![yy] + (yd * yd / 6.0)
                        else xd * xd + yd * yd + zd * zd
                        if ((!canyon && yd > -0.7 && test < 1.0) || (canyon && test < 1.0)) {
                            val block = buffer.getBlock(xx, yy, zz)
                            if (block == OldGeneratorBlockIds.GRASS) hasGrass = true
                            if (block == OldGeneratorBlockIds.STONE || block == OldGeneratorBlockIds.DIRT || block == OldGeneratorBlockIds.GRASS) {
                                if (yy < 10) {
                                    buffer.setBlock(xx, yy, zz, OldGeneratorBlockIds.LAVA)
                                } else {
                                    buffer.setBlock(xx, yy, zz, OldGeneratorBlockIds.AIR)
                                    if (hasGrass && yy > 0 && buffer.getBlock(xx, yy - 1, zz) == OldGeneratorBlockIds.DIRT) {
                                        buffer.setBlock(xx, yy - 1, zz, buffer.getBiome(xx, zz)!!.topBlockId)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        fun detectWater(buffer: OldGeneratorChunkBuffer, x0: Int, x1: Int, y0: Int, y1: Int, z0: Int, z1: Int): Boolean {
            for (xx in x0 until x1) {
                for (zz in z0 until z1) {
                    var yy = y1 + 1
                    while (yy >= y0 - 1) {
                        if (yy in 0 until OldGeneratorChunkBuffer.GEN_DEPTH) {
                            if (buffer.getBlock(xx, yy, zz) == OldGeneratorBlockIds.WATER) return true
                            if (yy != y0 - 1 && xx != x0 && xx != x1 - 1 && zz != z0 && zz != z1 - 1) yy = y0
                        }
                        yy--
                    }
                }
            }
            return false
        }

        private fun floor(value: Double): Int {
            val floor = value.toInt()
            return if (value < floor) floor - 1 else floor
        }
    }
}

internal class OldGeneratorCanyonCarver : OldGeneratorCarver() {
    private val rs = FloatArray(OldGeneratorChunkBuffer.GEN_DEPTH)

    override fun addFeature(startChunkX: Int, startChunkZ: Int, targetChunkX: Int, targetChunkZ: Int, buffer: OldGeneratorChunkBuffer) {
        if (random.nextInt(50) != 0) return

        val xCave = startChunkX * 16.0 + random.nextInt(16)
        val yCave = (random.nextInt(random.nextInt(40) + 8) + 20).toDouble()
        val zCave = startChunkZ * 16.0 + random.nextInt(16)
        val yRot = random.nextFloat() * PI.toFloat() * 2.0f
        val xRot = ((random.nextFloat() - 0.5f) * 2.0f) / 8.0f
        val thickness = (random.nextFloat() * 2.0f + random.nextFloat()) * 2.0f
        addTunnel(random.nextLong(), targetChunkX, targetChunkZ, buffer, xCave, yCave, zCave, thickness, yRot, xRot, 0, 0, 3.0)
    }

    private fun addTunnel(
        tunnelSeed: Long, chunkX: Int, chunkZ: Int, buffer: OldGeneratorChunkBuffer,
        xCaveIn: Double, yCaveIn: Double, zCaveIn: Double,
        thickness: Float, yRotIn: Float, xRotIn: Float,
        stepIn: Int, distIn: Int, yScale: Double
    ) {
        val tunnelRandom = OldGeneratorNoise(tunnelSeed)
        val xMid = chunkX * 16.0 + 8.0
        val zMid = chunkZ * 16.0 + 8.0
        var yRota = 0.0f
        var xRota = 0.0f

        var dist = distIn
        if (dist <= 0) {
            val max = radius * 16 - 16
            dist = max - tunnelRandom.nextInt(max / 4)
        }

        var singleStep = false
        var step = stepIn
        if (step == -1) {
            step = dist / 2
            singleStep = true
        }

        var f = 1.0f
        for (i in 0 until OldGeneratorChunkBuffer.GEN_DEPTH) {
            if (i == 0 || tunnelRandom.nextInt(3) == 0) {
                f = 1.0f + tunnelRandom.nextFloat() * tunnelRandom.nextFloat()
            }
            rs[i] = f * f
        }

        var xCave = xCaveIn; var yCave = yCaveIn; var zCave = zCaveIn
        var yRot = yRotIn; var xRot = xRotIn

        while (step < dist) {
            var rad = 1.5 + sin(step * PI / dist) * thickness
            var yRad = rad * yScale

            rad *= tunnelRandom.nextFloat() * 0.25 + 0.75
            yRad *= tunnelRandom.nextFloat() * 0.25 + 0.75

            val xc = cos(xRot.toDouble()).toFloat()
            val xs = sin(xRot.toDouble()).toFloat()
            xCave += cos(yRot.toDouble()) * xc
            yCave += xs
            zCave += sin(yRot.toDouble()) * xc

            xRot *= 0.7f
            xRot += xRota * 0.05f
            yRot += yRota * 0.05f
            xRota *= 0.80f
            yRota *= 0.50f
            xRota += (tunnelRandom.nextFloat() - tunnelRandom.nextFloat()) * tunnelRandom.nextFloat() * 2.0f
            yRota += (tunnelRandom.nextFloat() - tunnelRandom.nextFloat()) * tunnelRandom.nextFloat() * 4.0f

            if (!singleStep && tunnelRandom.nextInt(4) == 0) { step++; continue }

            val xd = xCave - xMid
            val zd = zCave - zMid
            val remaining = dist - step
            val rr = thickness + 18.0
            if (xd * xd + zd * zd - remaining * remaining > rr * rr) return

            if (xCave < xMid - 16.0 - rad * 2.0 || zCave < zMid - 16.0 - rad * 2.0
                || xCave > xMid + 16.0 + rad * 2.0 || zCave > zMid + 16.0 + rad * 2.0) { step++; continue }

            val x0 = max(0, floor(xCave - rad) - chunkX * 16 - 1)
            val x1 = min(16, floor(xCave + rad) - chunkX * 16 + 1)
            val y0 = max(1, floor(yCave - yRad) - 1)
            val y1 = min(OldGeneratorChunkBuffer.GEN_DEPTH - 8, floor(yCave + yRad) + 1)
            val z0 = max(0, floor(zCave - rad) - chunkZ * 16 - 1)
            val z1 = min(16, floor(zCave + rad) - chunkZ * 16 + 1)

            if (OldGeneratorCaveCarver.detectWater(buffer, x0, x1, y0, y1, z0, z1)) { step++; continue }

            OldGeneratorCaveCarver.carveEllipsoid(buffer, chunkX, chunkZ, xCave, yCave, zCave, rad, yRad, x0, x1, y0, y1, z0, z1, true, rs)
            if (singleStep) break
            step++
        }
    }

    companion object {
        private fun floor(value: Double): Int {
            val floor = value.toInt()
            return if (value < floor) floor - 1 else floor
        }
    }
}