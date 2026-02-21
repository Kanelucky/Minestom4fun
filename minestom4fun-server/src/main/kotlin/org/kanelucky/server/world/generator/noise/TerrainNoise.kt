package org.kanelucky.server.world.generator.noise

import net.minestom.server.instance.block.Block
import org.kanelucky.server.config.ConfigManager
import org.kanelucky.server.world.generator.WorldConstants.WATER_LEVEL

/**
 * @author Kanelucky
 */
object TerrainNoise {

    private val noise = FastNoise(ConfigManager.worldSettings.seed)
    private val climate = ClimateNoise(NoiseConfig.seed + 1L)

    private const val BASE_HEIGHT = 63
    private val FREQ = doubleArrayOf(0.003, 0.01, 0.05)
    private val AMP  = doubleArrayOf(35.0, 12.0, 4.0)

    fun terrainHeight(x: Int, z: Int): Int {
        var height = BASE_HEIGHT.toDouble()
        for (i in FREQ.indices) {
            height += noise.noise(x * FREQ[i], z * FREQ[i]) * AMP[i]
        }
        return height.toInt()
    }

    fun surfaceBlock(x: Int, z: Int): Block {
        val height = terrainHeight(x, z)
        val humidity = climate.humidity(x, z)

        return when {
            height <= WATER_LEVEL     -> Block.WATER
            height <= WATER_LEVEL + 2 -> Block.SAND
            humidity < -0.2           -> Block.SAND
            else                      -> Block.GRASS_BLOCK
        }
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