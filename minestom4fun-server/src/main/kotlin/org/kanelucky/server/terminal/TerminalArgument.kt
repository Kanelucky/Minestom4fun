package org.kanelucky.server.terminal

sealed class TerminalArgument {
    data class Literal(val value: String) : TerminalArgument()
    object PlayerTarget : TerminalArgument()
    object ItemMaterial : TerminalArgument()
    object Number : TerminalArgument()
    object FreeText : TerminalArgument()
}