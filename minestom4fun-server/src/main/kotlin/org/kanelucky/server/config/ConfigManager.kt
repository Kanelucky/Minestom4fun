package org.kanelucky.server.config

import org.kanelucky.server.config.network.NetworkSettings
import org.kanelucky.server.config.server.ServerSettings
import org.kanelucky.server.config.world.WorldSettings

import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Kanelucky
 */
object ConfigManager {

    lateinit var serverSettings: ServerSettings
    lateinit var networkSettings: NetworkSettings
    lateinit var worldSettings: WorldSettings

    private val root = Path.of("config")

    fun init() {
        Files.createDirectories(root)

        serverSettings = ServerSettings()
        networkSettings = NetworkSettings()
        worldSettings = WorldSettings()
    }

    fun resolve(vararg paths: String): Path =
        paths.fold(root) { acc, p -> acc.resolve(p) }
}
