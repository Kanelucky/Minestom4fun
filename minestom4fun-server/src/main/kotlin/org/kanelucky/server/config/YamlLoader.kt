package org.kanelucky.server.config

import com.charleskorn.kaml.Yaml

import java.nio.file.Files
import java.nio.file.Path

object YamlLoader {

    private val yaml = Yaml(configuration = Yaml.default.configuration.copy(
        encodeDefaults = true
    ))

    inline fun <reified T> load(path: Path, default: T): T {
        if (Files.notExists(path)) {
            Files.createDirectories(path.parent)
            Files.writeString(path, yaml.encodeToString(default))
            return default
        }

        return yaml.decodeFromString(Files.readString(path))
    }
}
