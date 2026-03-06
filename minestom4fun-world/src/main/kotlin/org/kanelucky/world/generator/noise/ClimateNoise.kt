package org.kanelucky.world.generator.noise

import org.kanelucky.config.world.noise.NoiseConfig

/**
 * @author Kanelucky
 */
class ClimateNoise(seed: Long) {

    private val noise = FastNoise(seed)

    fun temperature(x: Int, z: Int): Double =
        noise.fractal2D(x * NoiseConfig.TEMP_SCALE, z * NoiseConfig.TEMP_SCALE, 3)

    fun humidity(x: Int, z: Int): Double =
        noise.fractal2D(x * NoiseConfig.HUMIDITY_SCALE, z * NoiseConfig.HUMIDITY_SCALE, 3)
}