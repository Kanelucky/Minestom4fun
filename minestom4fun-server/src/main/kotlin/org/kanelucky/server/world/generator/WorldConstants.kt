package org.kanelucky.server.world.generator

object WorldConstants {
    const val SEA_LEVEL = 64
    const val SURFACE_OFFSET = 30
    const val BASE_HEIGHT = 63

    const val WATER_LEVEL = 62
    const val RIVER_THRESHOLD = 0.045
    const val RIVER_DEPTH = 4

    val FREQ = doubleArrayOf(0.003, 0.01, 0.05)
    val AMP  = doubleArrayOf(35.0, 12.0, 4.0)

    const val TREE_CHANCE = 0.0025

    const val SEAGRASS_CHANCE = 0.18
    const val TALL_SEAGRASS_CHANCE = 0.35
    const val MIN_WATER_DEPTH = 3

    const val CAVE_THRESHOLD = 0.50
    const val TUNNEL_THRESHOLD = 0.62
    const val CAVE_MIN_Y = 8
    const val CAVE_SURFACE_BUFFER = 8


}