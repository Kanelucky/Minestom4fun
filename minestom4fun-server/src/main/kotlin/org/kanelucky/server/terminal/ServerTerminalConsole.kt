package org.kanelucky.server.terminal

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.CommandDispatcher

import java.io.BufferedReader
import java.io.InputStreamReader

class ServerTerminalConsole {
    fun startConsole(dispatcher: CommandDispatcher, sender: CommandSender) {
        Thread {
            val reader = BufferedReader(InputStreamReader(System.`in`))
            while (true) {
                val line = reader.readLine() ?: break
                if (line.isBlank()) continue
                dispatcher.execute(sender, line)
            }
        }.apply {
            name = "Console-Input"
            isDaemon = true
            start()
        }
    }
}