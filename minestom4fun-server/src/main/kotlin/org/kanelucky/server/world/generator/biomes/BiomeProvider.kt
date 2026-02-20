package org.kanelucky.server.world.generator.biomes

import org.kanelucky.server.world.generator.noise.ClimateNoise
import kotlin.math.abs

/**
 * @author Kanelucky
 */
class BiomeProvider(seed: Long) {

    private val climate = ClimateNoise(seed)

    fun getBiome(x: Int, z: Int): Biome {

        val temp = climate.temperature(x, z)
        val humidity = climate.humidity(x, z)

        if (abs(temp) < 0.03) return Biome.RIVER

        if (temp < -0.25) return Biome.OCEAN

        if (humidity > 0.2) return Biome.FOREST

        if (temp > 0.45) return Biome.HILLS

        return Biome.PLAINS
    }
}