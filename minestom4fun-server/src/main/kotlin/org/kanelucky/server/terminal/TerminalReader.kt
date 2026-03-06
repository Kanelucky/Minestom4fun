package org.kanelucky.server.terminal

import net.minestom.server.command.CommandManager
import org.jline.reader.*
import org.jline.terminal.TerminalBuilder
import java.util.concurrent.Executors

object TerminalReader {

    private val executor = Executors.newSingleThreadExecutor { r ->
        Thread(r, "console-thread").also { it.isDaemon = true }
    }

    fun start(commandManager: CommandManager) {
        executor.submit {
            val terminal = TerminalBuilder.builder().system(true).build()

            val completer = Completer { _, line, candidates ->
                val wordIndex = line.wordIndex()
                val currentWord = line.word()

                if (wordIndex == 0) {
                    val allCommands = commandManager.commands
                    println("DEBUG commands: ${allCommands.map { it.name }}") // xem log
                    allCommands
                        .filter { it.name.startsWith(currentWord) }
                        .forEach { candidates.add(Candidate(it.name)) }
                }
            }

            val reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .option(LineReader.Option.AUTO_FRESH_LINE, true)
                .option(LineReader.Option.COMPLETE_IN_WORD, true)
                .option(LineReader.Option.CASE_INSENSITIVE, true)
                .build()

            while (true) {
                val line = try {
                    reader.readLine("$ ")
                } catch (e: UserInterruptException) { continue }
                catch (e: EndOfFileException) { break }
                    ?: continue

                val trimmed = line.trim()
                if (trimmed.isEmpty()) continue
                commandManager.execute(commandManager.consoleSender, trimmed)
            }
        }
    }
}