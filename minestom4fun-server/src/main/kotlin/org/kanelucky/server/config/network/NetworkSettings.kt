package org.kanelucky.server.config.network

import kotlinx.serialization.Serializable

/**
 * @author Kanelucky
 */
@Serializable
data class NetworkSettings(
    var serverBrand: String = "Minestom4fun",
    val motd: String = "Welcome to my Minecraft server!",
)