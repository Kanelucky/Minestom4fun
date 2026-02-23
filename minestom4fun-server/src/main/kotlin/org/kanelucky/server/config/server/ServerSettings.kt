package org.kanelucky.server.config.server

import kotlinx.serialization.Serializable

/**
 * @author Kanelucky
 */
@Serializable
data class ServerSettings (
    var address: String = "0.0.0.0",
    var port: Int = 25565,
    var maxPlayers: Int = 20
)