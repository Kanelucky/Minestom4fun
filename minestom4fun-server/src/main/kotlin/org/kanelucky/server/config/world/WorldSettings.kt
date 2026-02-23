package org.kanelucky.server.config.world

import kotlinx.serialization.Serializable

/**
 * @author Kanelucky
 */
@Serializable
enum class WorldType {
    NORMAL,
    SUPERFLAT
}

@Serializable
data class WorldSettings(
    var worldType: WorldType = WorldType.NORMAL,
    var seed: Long = 1234567890,
    var superflat: SuperflatConfig = SuperflatConfig()
)

@Serializable
data class SuperflatConfig(
    val preset: String = "classic"
)
