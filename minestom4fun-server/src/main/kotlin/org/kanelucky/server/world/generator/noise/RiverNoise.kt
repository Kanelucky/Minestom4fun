package org.kanelucky.server.world.generator.noise

import org.kanelucky.server.config.ConfigManager

import kotlin.math.abs

/**
 * @author Kanelucky
 */
object RiverNoise {

    private val noise = FastNoise(ConfigManager.worldSettings.seed)

    fun sample(x: Int, z: Int): Double {
        return abs(
            noise.fractal2D(
                x * 0.005,
                z * 0.005,
                2
            )
        )
    }
}