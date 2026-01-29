package org.kanelucky.config.commands

data class CommandsConfig(
    val enabled: Boolean = true,
    val gamemode: Boolean = true,
    val version: Boolean = true
)