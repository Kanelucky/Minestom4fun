package org.kanelucky.server.config

import java.nio.file.Files
import java.nio.file.Path

object ConfigManager {

    private val root = Path.of("config")

    fun init() {
        Files.createDirectories(root)
    }

    fun resolve(vararg paths: String): Path =
        paths.fold(root) { acc, p -> acc.resolve(p) }
}
