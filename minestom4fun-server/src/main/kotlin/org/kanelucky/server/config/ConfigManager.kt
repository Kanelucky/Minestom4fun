package org.kanelucky.server.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
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
    private val worldsRoot = Path.of("worlds")
    private val yaml = Yaml.default
    private val json = Json { prettyPrint = true }

    fun init() {
        Files.createDirectories(root)
        Files.createDirectories(worldsRoot)

        serverSettings = loadOrCreate("server/server-settings.yml", ServerSettings())
        networkSettings = loadOrCreate("network/network-settings.yml", NetworkSettings())
        worldSettings = loadWorldSettings()
    }

    private fun loadWorldSettings(): WorldSettings {
        val file = worldsRoot.resolve("world-settings.json")
        if (!Files.exists(file)) {
            val default = WorldSettings()
            Files.writeString(file, json.encodeToString(WorldSettings.serializer(), default))
            return default
        }
        return json.decodeFromString(WorldSettings.serializer(), Files.readString(file))
    }

    private inline fun <reified T : Any> loadOrCreate(path: String, default: T): T {
        val file = resolve(path)
        Files.createDirectories(file.parent)

        if (!Files.exists(file)) {
            Files.writeString(file, yaml.encodeToString(serializer(), default))
            return default
        }

        return yaml.decodeFromString(serializer(), Files.readString(file))
    }

    fun resolve(vararg paths: String): Path =
        paths.fold(root) { acc, p -> acc.resolve(p) }
}
