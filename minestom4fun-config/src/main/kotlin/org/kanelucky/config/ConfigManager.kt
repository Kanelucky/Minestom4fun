package org.kanelucky.config

import eu.okaeri.configs.ConfigManager as OkaeriConfigManager
import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer
import org.kanelucky.config.network.NetworkSettings
import org.kanelucky.config.server.ServerSettings
import org.kanelucky.config.world.WorldSettings

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

        serverSettings = load("server/server-settings.yml")
        networkSettings = load("network/network-settings.yml")
        worldSettings = load("worlds/world-settings.yml")
    }

    private inline fun <reified T : OkaeriConfig> load(path: String): T {
        val file = resolve(path)
        Files.createDirectories(file.parent)

        return OkaeriConfigManager.create(T::class.java) {
            it.withConfigurer(YamlSnakeYamlConfigurer())
            it.withBindFile(file)
            it.saveDefaults()
            it.load(true)
        }
    }

    fun resolve(vararg paths: String): Path =
        paths.fold(root) { acc, p -> acc.resolve(p) }
}