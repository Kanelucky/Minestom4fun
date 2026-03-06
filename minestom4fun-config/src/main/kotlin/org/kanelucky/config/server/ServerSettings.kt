package org.kanelucky.config.server

import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.annotation.Comment

/**
 * @author Kanelucky
 */
class ServerSettings : OkaeriConfig() {
    @Comment("Server bind address")
    var address: String = "0.0.0.0"

    @Comment("Server port")
    var port: Int = 25565

    @Comment("Maximum number of players")
    var maxPlayers: Int = 20
}