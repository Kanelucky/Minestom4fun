package org.kanelucky.world.generator.noise

import org.kanelucky.config.ConfigManager

/**
 * Old Java Random implementation for legacy noise generation
 *
 * Originally developed in OldConsoleWorldgen(https://github.com/zernix2077/OldConsoleWorldgen)
 *
 * Ported to Kotlin by Kanelucky
 */
internal class OldGeneratorNoise(seed: Long) {
    private var seed: Long = 0
    private var haveNextNextGaussian = false
    private var nextNextGaussian = 0.0

    init { setSeed(seed) }

    fun setSeed(seed: Long) {
        this.seed = (seed xor 0x5DEECE66DL) and ((1L shl 48) - 1)
        haveNextNextGaussian = false
    }

    fun nextInt(): Int = next(32)

    fun nextInt(bound: Int): Int {
        require(bound > 0) { "bound must be positive" }
        if (bound and -bound == bound) return ((bound * next(31).toLong()) shr 31).toInt()
        var bits: Int
        var value: Int
        do {
            bits = next(31)
            value = bits % bound
        } while (bits - value + (bound - 1) < 0)
        return value
    }

    fun nextLong(): Long = (next(32).toLong() shl 32) + next(32)
    fun nextFloat(): Float = next(24) / (1 shl 24).toFloat()
    fun nextDouble(): Double = ((next(26).toLong() shl 27) + next(27)) / (1L shl 53).toDouble()

    private fun next(bits: Int): Int {
        seed = (seed * 0x5DEECE66DL + 0xBL) and ((1L shl 48) - 1)
        return (seed ushr (48 - bits)).toInt()
    }
}

/**
 * Old Java Random implementation for legacy noise generation
 *
 * Originally developed in OldConsoleWorldgen(https://github.com/zernix2077/OldConsoleWorldgen)
 *
 * Ported to Kotlin by Kanelucky
 */
internal class OldGeneratorImprovedNoise(random: OldGeneratorNoise) {
    private val p = IntArray(512)
    private val xo: Double = random.nextDouble() * 256.0
    private val yo: Double = random.nextDouble() * 256.0
    private val zo: Double = random.nextDouble() * 256.0

    init {
        for (i in 0 until 256) p[i] = i
        for (i in 0 until 256) {
            val j = random.nextInt(256 - i) + i
            val value = p[i]
            p[i] = p[j]
            p[j] = value
            p[i + 256] = p[i]
        }
    }

    fun add(buffer: DoubleArray, x: Double, y: Double, z: Double, xSize: Int, ySize: Int, zSize: Int, xs: Double, ys: Double, zs: Double, pow: Double) {
        val scale = 1.0 / pow

        if (ySize == 1) {
            var index = 0
            for (xx in 0 until xSize) {
                var xPos = x + xx * xs + xo
                val xf = floor(xPos); val X = xf and 255; xPos -= xf
                val u = fade(xPos)
                for (zz in 0 until zSize) {
                    var zPos = z + zz * zs + zo
                    val zf = floor(zPos); val Z = zf and 255; zPos -= zf
                    val w = fade(zPos)
                    val A = p[X]; val AA = p[A] + Z; val B = p[X + 1]; val BA = p[B] + Z
                    val vv0 = lerp(u, grad2(p[AA], xPos, zPos), grad(p[BA], xPos - 1.0, 0.0, zPos))
                    val vv2 = lerp(u, grad(p[AA + 1], xPos, 0.0, zPos - 1.0), grad(p[BA + 1], xPos - 1.0, 0.0, zPos - 1.0))
                    buffer[index++] += lerp(w, vv0, vv2) * scale
                }
            }
            return
        }

        var index = 0
        var yOld = Int.MIN_VALUE
        var A = 0; var AA = 0; var AB = 0; var B = 0; var BA = 0; var BB = 0
        var vv0 = 0.0; var vv1 = 0.0; var vv2 = 0.0; var vv3 = 0.0

        for (xx in 0 until xSize) {
            var xPos = x + xx * xs + xo
            val xf = floor(xPos); val X = xf and 255; xPos -= xf
            val u = fade(xPos)
            for (zz in 0 until zSize) {
                var zPos = z + zz * zs + zo
                val zf = floor(zPos); val Z = zf and 255; zPos -= zf
                val w = fade(zPos)
                for (yy in 0 until ySize) {
                    var yPos = y + yy * ys + yo
                    val yf = floor(yPos); val Y = yf and 255; yPos -= yf
                    val v = fade(yPos)
                    if (yy == 0 || Y != yOld) {
                        yOld = Y
                        A = p[X] + Y; AA = p[A] + Z; AB = p[A + 1] + Z
                        B = p[X + 1] + Y; BA = p[B] + Z; BB = p[B + 1] + Z
                        vv0 = lerp(u, grad(p[AA], xPos, yPos, zPos), grad(p[BA], xPos - 1.0, yPos, zPos))
                        vv1 = lerp(u, grad(p[AB], xPos, yPos - 1.0, zPos), grad(p[BB], xPos - 1.0, yPos - 1.0, zPos))
                        vv2 = lerp(u, grad(p[AA + 1], xPos, yPos, zPos - 1.0), grad(p[BA + 1], xPos - 1.0, yPos, zPos - 1.0))
                        vv3 = lerp(u, grad(p[AB + 1], xPos, yPos - 1.0, zPos - 1.0), grad(p[BB + 1], xPos - 1.0, yPos - 1.0, zPos - 1.0))
                    }
                    buffer[index++] += lerp(v, lerp(v, vv0, vv1), lerp(v, vv2, vv3)) * scale
                }
            }
        }
    }

    private fun floor(value: Double): Int {
        val floor = value.toInt()
        return if (value < floor) floor - 1 else floor
    }

    private fun fade(value: Double) = value * value * value * (value * (value * 6.0 - 15.0) + 10.0)
    private fun lerp(t: Double, a: Double, b: Double) = a + t * (b - a)

    private fun grad2(hash: Int, x: Double, z: Double): Double {
        val h = hash and 15
        val u = (1 - ((h and 8) shr 3)) * x
        val v = if (h < 4) 0.0 else if (h == 12 || h == 14) x else z
        return (if ((h and 1) == 0) u else -u) + (if ((h and 2) == 0) v else -v)
    }

    private fun grad(hash: Int, x: Double, y: Double, z: Double): Double {
        val h = hash and 15
        val u = if (h < 8) x else y
        val v = if (h < 4) y else if (h == 12 || h == 14) x else z
        return (if ((h and 1) == 0) u else -u) + (if ((h and 2) == 0) v else -v)
    }
}

/**
 * Old Java Random implementation for legacy noise generation
 *
 * Originally developed in OldConsoleWorldgen(https://github.com/zernix2077/OldConsoleWorldgen)
 *
 * Ported to Kotlin by Kanelucky
 */
internal class OldGeneratorPerlinNoise(random: OldGeneratorNoise, levels: Int) {
    private val noiseLevels = Array(levels) { OldGeneratorImprovedNoise(random) }

    fun getRegion(buffer: DoubleArray?, x: Int, y: Int, z: Int, xSize: Int, ySize: Int, zSize: Int, xScale: Double, yScale: Double, zScale: Double): DoubleArray {
        val result = if (buffer == null || buffer.size < xSize * ySize * zSize)
            DoubleArray(xSize * ySize * zSize)
        else buffer.also { it.fill(0.0) }

        var pow = 1.0
        for (noiseLevel in noiseLevels) {
            var xx = x * pow * xScale
            val yy = y * pow * yScale
            var zz = z * pow * zScale
            var xb = lfloor(xx); var zb = lfloor(zz)
            xx -= xb; zz -= zb
            xb %= 16777216L; zb %= 16777216L
            xx += xb; zz += zb
            noiseLevel.add(result, xx, yy, zz, xSize, ySize, zSize, xScale * pow, yScale * pow, zScale * pow, pow)
            pow /= 2.0
        }
        return result
    }

    fun getRegion(buffer: DoubleArray?, x: Int, z: Int, xSize: Int, zSize: Int, xScale: Double, zScale: Double): DoubleArray =
        getRegion(buffer, x, 10, z, xSize, 1, zSize, xScale, 1.0, zScale)

    private fun lfloor(value: Double): Long {
        val floor = value.toLong()
        return if (value < floor) floor - 1L else floor
    }
}