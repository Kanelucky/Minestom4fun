package org.kanelucky.server.terminal

interface TerminalCommand {
    val name: String
    val description: String
    val arguments: List<TerminalArgument>

    fun execute(args: List<String>)
}