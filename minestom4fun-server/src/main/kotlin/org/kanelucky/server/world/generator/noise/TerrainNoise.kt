package org.kanelucky.server.world.generator.noise

import org.kanelucky.server.config.ConfigManager

/**
 * @author Kanelucky
 */
object TerrainNoise {

    private val noise = FastNoise(ConfigManager.serverSettings.seed)

    fun terrainHeight(x: Int, z: Int): Int {

        val continental = noise.fractal2D(x * 0.0008, z * 0.0008, 4) * 35
        val hills = noise.fractal2D(x * 0.01, z * 0.01, 3) * 12
        val detail = noise.fractal2D(x * 0.05, z * 0.05, 2) * 3

        return (64 + continental + hills + detail).toInt()
    }

    fun cave(x: Int, y: Int, z: Int): Double {
        return noise.fractal3D(
            x * 0.05,
            y * 0.05,
            z * 0.05,
            3
        )
    }

    fun riverNoise(x: Int, z: Int): Double {
        return noise.noise(x * 0.004, z * 0.004)
    }
}