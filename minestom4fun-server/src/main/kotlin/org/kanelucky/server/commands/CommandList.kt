package org.kanelucky.server.commands

/**
 * @author Kanelucky
 */
object CommandList {

    val commands = listOf(
        "/help" to "Shows this help message",
        "/gamemode <mode> [target]" to "Sets the gamemode of a player",
        "/kill <target>" to "Kills a player",
        "/op <target>" to "Grants operator status to a player",
        "/tps" to "Shows the server TPS",
        "/version" to "Shows the server version"
    )
}