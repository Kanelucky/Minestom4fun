package org.kanelucky.server.world.generator.noise

import org.kanelucky.server.config.ConfigManager

/**
 * @author Kanelucky
 */
object NoiseConfig {

    val seed: Long
        get() = ConfigManager.worldSettings.seed

    const val CONTINENT_SCALE = 0.0008
    const val HILL_SCALE = 0.01
    const val DETAIL_SCALE = 0.05

    const val RIVER_SCALE = 0.003

    const val CAVE_SCALE = 0.05
    const val CAVE_SCALE_MAIN = 0.035
    const val CAVE_SCALE_TUNNEL = 0.12

    const val TEMP_SCALE = 0.0015
    const val HUMIDITY_SCALE = 0.0015
}