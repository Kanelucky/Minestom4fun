package org.kanelucky.world.generator.terrain

/**
 * @author Kanelucky
 */
object TerrainSplines {

    fun calculateHeight(
        continental: Double,
        erosion: Double,
        peaks: Double,
        blend: Double
    ): Int {
        val baseHeight = continentalSpline(continental)
        val erosionMult = erosionSpline(erosion)
        val peakOffset = peaksSpline(peaks)
        val blendFactor = 1.0 + blend * 0.1

        return ((baseHeight * erosionMult + peakOffset) * blendFactor).toInt()
            .coerceIn(-64, 320)
    }

    private fun continentalSpline(v: Double): Double = when {
        v < -0.8 -> lerp(v, -1.0, -0.8, 20.0, 40.0)
        v < -0.4 -> lerp(v, -0.8, -0.4, 40.0, 55.0)
        v < 0.0  -> lerp(v, -0.4, 0.0, 55.0, 63.0)
        v < 0.3  -> lerp(v, 0.0, 0.3, 63.0, 72.0)
        v < 0.6  -> lerp(v, 0.3, 0.6, 72.0, 100.0)
        else     -> lerp(v, 0.6, 1.0, 100.0, 200.0)
    }

    private fun erosionSpline(v: Double): Double = when {
        v < -0.6 -> 1.5
        v < -0.2 -> lerp(v, -0.6, -0.2, 1.5, 1.2)
        v < 0.2  -> lerp(v, -0.2, 0.2, 1.2, 1.0)
        v < 0.6  -> lerp(v, 0.2, 0.6, 1.0, 0.7)
        else     -> lerp(v, 0.6, 1.0, 0.7, 0.4)
    }

    private fun peaksSpline(v: Double): Double = when {
        v < -0.6 -> -20.0
        v < 0.0  -> lerp(v, -0.6, 0.0, -20.0, 0.0)
        v < 0.6  -> lerp(v, 0.0, 0.6, 0.0, 30.0)
        else     -> 30.0
    }

    private fun lerp(v: Double, min: Double, max: Double, minOut: Double, maxOut: Double): Double {
        val t = (v - min) / (max - min)
        return minOut + t * (maxOut - minOut)
    }
}