package org.kanelucky.server

import io.github.togar2.pvp.MinestomPvP
import io.github.togar2.pvp.feature.CombatFeatures

import net.hollowcube.polar.PolarLoader

import net.minestom.server.MinecraftServer
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceContainer

import org.kanelucky.server.commands.CommandRegistry
import org.kanelucky.server.event.EventRegistry
import org.kanelucky.server.network.status.ServerListPing
import org.kanelucky.server.terminal.ServerTerminalConsole
import org.kanelucky.server.world.generator.SuperFlatGenerator.SuperFlatGenerator
import org.kanelucky.server.world.generator.SuperFlatGenerator.preset.BottomlessPit

import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Kanelucky
 */
object Minestom4fun {

    lateinit var instanceContainer: InstanceContainer
    lateinit var globalEventHandler: GlobalEventHandler
    lateinit var minecraftServer: MinecraftServer

    @JvmStatic
    fun main() {

        minecraftServer = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()

        instanceContainer = instanceManager.createInstanceContainer()

        // Loading world
        Files.createDirectories(Path.of("worlds"))
        val polarPath = Path.of("worlds/world.polar")
        instanceContainer.setChunkLoader(PolarLoader(polarPath))

        // Set the ChunkGenerator
        instanceContainer.setGenerator(SuperFlatGenerator(BottomlessPit()))

        globalEventHandler = MinecraftServer.getGlobalEventHandler()

        //Register events
        EventRegistry.register(handler = globalEventHandler)

        // Register commands
        CommandRegistry.initialize()

        // MinestomPvP event
        MinestomPvP.init()
        val modern = CombatFeatures.modernVanilla()
        globalEventHandler.addChild(modern.createNode())

        ServerListPing.register()

        // Saving world
        instanceContainer.saveChunksToStorage()

        val commandManager = MinecraftServer.getCommandManager()
        val dispatcher = commandManager.dispatcher
        val console = commandManager.consoleSender

        ServerTerminalConsole().startConsole(dispatcher, console)

        minecraftServer.start("0.0.0.0", 25565)
    }
}