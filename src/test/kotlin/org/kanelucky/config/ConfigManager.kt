package org.kanelucky.config



import org.kanelucky.config.commands.CommandsConfig
import org.kanelucky.config.server.ServerConfig

import java.io.File

object ConfigManager {
    private const val DEFAULT_SERVER_CONFIG = """
# ============================
# Minestom4fun Server Config
# ============================

address: 0.0.0.0
port: 25565
"""
    private const val DEFAULT_COMMANDS_CONFIG = """
# ============================
# Minestom4fun Commands Config
# ============================

commands:
    enabled: true
    gamemode:
        enabled: true
    version:
        enabled: true
"""

    lateinit var server: ServerConfig
        private set

    lateinit var commands: CommandsConfig
        private set

    fun loadAll() {
        server = loadYaml(
            "config/server/server.yaml",
            DEFAULT_SERVER_CONFIG
        )

        commands = loadYaml(
            "config/commands/commands.yaml",
            DEFAULT_COMMANDS_CONFIG
        )
    }

    private inline fun <reified T> loadYaml(
        path: String,
        defaultContent: String
    ): T {
        val file = File(path)

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText(defaultContent)

            println("Generated missing config: ${file.path}")
        }

        return Yaml.decodeFromString(file.readText())
    }
}