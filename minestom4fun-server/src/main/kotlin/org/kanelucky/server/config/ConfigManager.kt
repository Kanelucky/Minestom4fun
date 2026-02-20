package org.kanelucky.server.config

import org.kanelucky.server.config.server.ServerSettings

import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Kanelucky
 */
object ConfigManager {

    lateinit var serverSettings: ServerSettings

    private val root = Path.of("config")

    fun init() {
        Files.createDirectories(root)

        serverSettings = ServerSettings()
    }

    fun resolve(vararg paths: String): Path =
        paths.fold(root) { acc, p -> acc.resolve(p) }
}
