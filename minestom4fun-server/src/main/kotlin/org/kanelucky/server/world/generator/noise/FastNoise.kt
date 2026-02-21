package org.kanelucky.server.world.generator.noise

import kotlin.math.floor

/**
 * Fast gradient noise implementation optimized for terrain generation
 *
 * Provides high-performance 2D and 3D gradient noise with fractal
 * sampling utilities, designed for procedural world generation
 *
 * Ported from SwiftMC concepts:
 * https://github.com/XDPXI/SwiftMC
 *
 * Original concept & implementation: XDPXI
 * Improvements, optimization & Kotlin adaptation: Kanelucky
 *
 */
class FastNoise(seed: Long) {


    private val perm = IntArray(512)

    init {
        val p = IntArray(256) { it }

        var state = seed
        for (i in 255 downTo 1) {
            state = state * 6364136223846793005L + 1442695040888963407L
            val j = ((state ushr 32) % (i + 1)).toInt()
            val tmp = p[i]
            p[i] = p[j]
            p[j] = tmp
        }

        for (i in 0 until 512)
            perm[i] = p[i and 255]
    }


    fun noise(x: Double, y: Double): Double {
        val xi = floor(x).toInt() and 255
        val yi = floor(y).toInt() and 255

        val xf = x - floor(x)
        val yf = y - floor(y)

        val u = fade(xf)
        val v = fade(yf)

        val aa = perm[perm[xi] + yi]
        val ab = perm[perm[xi] + yi + 1]
        val ba = perm[perm[xi + 1] + yi]
        val bb = perm[perm[xi + 1] + yi + 1]

        val x1 = lerp(u, grad2(aa, xf, yf), grad2(ba, xf - 1, yf))
        val x2 = lerp(u, grad2(ab, xf, yf - 1), grad2(bb, xf - 1, yf - 1))

        return lerp(v, x1, x2)
    }


    fun noise(x: Double, y: Double, z: Double): Double {
        val xi = floor(x).toInt() and 255
        val yi = floor(y).toInt() and 255
        val zi = floor(z).toInt() and 255

        val xf = x - floor(x)
        val yf = y - floor(y)
        val zf = z - floor(z)

        val u = fade(xf)
        val v = fade(yf)
        val w = fade(zf)

        val aaa = perm[perm[perm[xi] + yi] + zi]
        val aba = perm[perm[perm[xi] + yi + 1] + zi]
        val aab = perm[perm[perm[xi] + yi] + zi + 1]
        val abb = perm[perm[perm[xi] + yi + 1] + zi + 1]
        val baa = perm[perm[perm[xi + 1] + yi] + zi]
        val bba = perm[perm[perm[xi + 1] + yi + 1] + zi]
        val bab = perm[perm[perm[xi + 1] + yi] + zi + 1]
        val bbb = perm[perm[perm[xi + 1] + yi + 1] + zi + 1]

        val x1 = lerp(u, grad3(aaa, xf, yf, zf), grad3(baa, xf - 1, yf, zf))
        val x2 = lerp(u, grad3(aba, xf, yf - 1, zf), grad3(bba, xf - 1, yf - 1, zf))
        val y1 = lerp(v, x1, x2)

        val x3 = lerp(u, grad3(aab, xf, yf, zf - 1), grad3(bab, xf - 1, yf, zf - 1))
        val x4 = lerp(u, grad3(abb, xf, yf - 1, zf - 1), grad3(bbb, xf - 1, yf - 1, zf - 1))
        val y2 = lerp(v, x3, x4)

        return lerp(w, y1, y2)
    }

    fun fractal2D(
        x: Double,
        z: Double,
        octaves: Int,
        persistence: Double = 0.5,
        lacunarity: Double = 2.0
    ): Double {

        var amplitude = 1.0
        var frequency = 1.0
        var sum = 0.0
        var max = 0.0

        repeat(octaves) {
            sum += noise(x * frequency, z * frequency) * amplitude
            max += amplitude
            amplitude *= persistence
            frequency *= lacunarity
        }

        return sum / max
    }

    fun fractal3D(
        x: Double,
        y: Double,
        z: Double,
        octaves: Int
    ): Double {
        var amp = 1.0
        var freq = 1.0
        var sum = 0.0
        var max = 0.0

        repeat(octaves) {
            sum += noise(x * freq, y * freq, z * freq) * amp
            max += amp
            amp *= 0.5
            freq *= 2.0
        }

        return sum / max
    }

    private fun fade(t: Double) =
        t * t * t * (t * (t * 6 - 15) + 10)

    private fun lerp(
        t: Double,
        a: Double,
        b: Double
    ) =
        a + t * (b - a)

    private fun grad2(
        hash: Int,
        x: Double,
        y: Double
    ): Double {
        return when (hash and 3) {
            0 -> x + y
            1 -> -x + y
            2 -> x - y
            else -> -x - y
        }
    }

    private fun grad3(
        hash: Int,
        x: Double,
        y: Double,
        z: Double
    ): Double {
        val h = hash and 15
        val u = if (h < 8) x else y
        val v = if (h < 4) y else if (h == 12 || h == 14) x else z
        return ((if (h and 1 == 0) u else -u) +
                (if (h and 2 == 0) v else -v))
    }


}