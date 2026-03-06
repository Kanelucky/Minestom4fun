package org.kanelucky.server.terminal

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.item.Material
import org.jline.reader.*
import org.jline.reader.impl.completer.StringsCompleter
import org.jline.terminal.TerminalBuilder
import java.util.concurrent.Executors

object TerminalManager {

    private val commands = mutableMapOf<String, TerminalCommand>()
    private val executor = Executors.newSingleThreadExecutor { r ->
        Thread(r, "console-thread").also { it.isDaemon = true }
    }

    fun register(command: TerminalCommand) {
        commands[command.name] = command
    }

    fun start() {
        executor.submit { runConsole() }
    }

    private fun runConsole() {
        val terminal = TerminalBuilder.builder()
            .system(true)
            .jna(true)
            .build()
        println("Terminal: ${terminal.type}, class: ${terminal.javaClass.simpleName}")

        val completer = Completer { reader, line, candidates ->
            val words = line.words()
            val wordIndex = line.wordIndex()

            if (wordIndex == 0) {
                // Complete command names
                commands.keys
                    .filter { it.startsWith(line.word()) }
                    .forEach { candidates.add(Candidate(it)) }
                return@Completer
            }

            val commandName = words.getOrNull(0) ?: return@Completer
            val command = commands[commandName] ?: return@Completer
            val argIndex = wordIndex - 1
            val argument = command.arguments.getOrNull(argIndex) ?: return@Completer

            when (argument) {
                is TerminalArgument.Literal -> {
                    if (argument.value.startsWith(line.word())) {
                        candidates.add(Candidate(argument.value))
                    }
                }
                is TerminalArgument.PlayerTarget -> {
                    onlinePlayers()
                        .filter { it.startsWith(line.word()) }
                        .forEach { candidates.add(Candidate(it)) }
                }
                is TerminalArgument.ItemMaterial -> {
                    Material.values()
                        .map { it.name().lowercase() }
                        .filter { it.startsWith(line.word()) }
                        .forEach { candidates.add(Candidate(it)) }
                }
                is TerminalArgument.Number -> candidates.add(Candidate("1"))
                is TerminalArgument.FreeText -> {}
            }
        }

        val reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .completer(completer)
            .build()
            .also { it.unsetOpt(LineReader.Option.INSERT_TAB) }

        while (true) {
            val line = try {
                reader.readLine("$ ")
            } catch (e: UserInterruptException) {
                continue
            } catch (e: EndOfFileException) {
                break
            } ?: continue

            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            val parts = trimmed.split(" ").filter { it.isNotEmpty() }
            val commandName = parts[0]
            val args = parts.drop(1)

            val command = commands[commandName]
            if (command == null) {
                println("Unknown command: $commandName")
                println("Available: ${commands.keys.joinToString(", ")}")
            } else {
                runCatching { command.execute(args) }
                    .onFailure { println("Error: ${it.message}") }
            }
        }
    }

    private fun onlinePlayers(): List<String> =
        MinecraftServer.getConnectionManager().onlinePlayers.map { it.username }
}