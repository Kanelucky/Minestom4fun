package org.kanelucky.config.network

import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.annotation.Comment

/**
 * @author Kanelucky
 */
class NetworkSettings : OkaeriConfig() {
    @Comment("Server brand name shown in client")
    var serverBrand: String = "Minestom4fun"

    @Comment("Server MOTD shown in server list")
    var motd: String = "Welcome to my Minecraft server!"
}