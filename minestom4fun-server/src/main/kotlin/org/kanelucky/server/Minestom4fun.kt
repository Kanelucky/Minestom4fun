package org.kanelucky.server


import io.github.togar2.fluids.MinestomFluids
import io.github.togar2.pvp.MinestomPvP
import io.github.togar2.pvp.feature.CombatFeatures

import net.hollowcube.polar.PolarLoader
import net.minestom.server.MinecraftServer
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.item.Material

import org.kanelucky.server.commands.CommandRegistry
import org.kanelucky.server.config.ConfigManager
import org.kanelucky.server.config.ConfigManager.serverSettings
import org.kanelucky.server.events.EventRegistry
import org.kanelucky.server.log.ServerStartupLog
import org.kanelucky.server.network.NetworkRegistry
import org.kanelucky.server.terminal.ServerTerminalConsole
import org.kanelucky.server.world.blocks.WorldBlockRegistry
import org.kanelucky.server.world.generator.NormalGenerator
import org.kanelucky.server.world.generator.OverworldGenerator

import io.github.togar2.fluids.FluidState
import io.github.togar2.fluids.WaterlogHandler
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.item.ItemStack
import org.kanelucky.server.world.fluid.FluidEventHandler

import java.nio.file.Files
import java.nio.file.Path


/**
 * Main class of Minestom4fun
 *
 * @author Kanelucky
 */
object Minestom4fun {

    lateinit var instanceContainer: InstanceContainer
    lateinit var globalEventHandler: GlobalEventHandler
    lateinit var minecraftServer: MinecraftServer

    @JvmStatic
    fun main() {

        ConfigManager.init()

        ServerStartupLog.print()

        minecraftServer = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()

        instanceContainer = instanceManager.createInstanceContainer()

        // Loading world
        Files.createDirectories(Path.of("worlds"))
        val polarPath = Path.of("worlds/world.polar")
        instanceContainer.setChunkLoader(PolarLoader(polarPath))

        // Set the ChunkGenerator
        instanceContainer.setGenerator(NormalGenerator())

        instanceContainer.setChunkSupplier(::LightingChunk)

        globalEventHandler = MinecraftServer.getGlobalEventHandler()

        // Register events
        EventRegistry.register(handler = globalEventHandler)

        // Register commands
        CommandRegistry.initialize()

        // MinestomPvP event
        MinestomPvP.init()
        val modern = CombatFeatures.modernVanilla()
        globalEventHandler.addChild(modern.createNode())

        // Network
        NetworkRegistry.initialize()

        WorldBlockRegistry.initialize()

        // Saving world
        instanceContainer.saveChunksToStorage()

        val commandManager = MinecraftServer.getCommandManager()
        val dispatcher = commandManager.dispatcher
        val console = commandManager.consoleSender

        ServerTerminalConsole().startConsole(dispatcher, console)

        MinestomFluids.init()
        FluidEventHandler.register(globalEventHandler)
        MinecraftServer.getGlobalEventHandler().addChild(MinestomFluids.events())

        minecraftServer.start(serverSettings.address, serverSettings.port)

        ServerStartupLog.done()
    }
}