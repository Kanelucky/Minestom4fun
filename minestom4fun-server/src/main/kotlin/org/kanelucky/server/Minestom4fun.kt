package org.kanelucky.server

import io.github.togar2.fluids.MinestomFluids
import io.github.togar2.pvp.MinestomPvP
import io.github.togar2.pvp.feature.CombatFeatures

import net.hollowcube.polar.PolarLoader

import net.minestom.server.MinecraftServer
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.LightingChunk
import org.kanelucky.gui.Dashboard

import org.kanelucky.server.commands.CommandRegistry
import org.kanelucky.server.config.ConfigManager
import org.kanelucky.server.config.ConfigManager.serverSettings
import org.kanelucky.server.config.world.WorldType
import org.kanelucky.server.events.EventRegistry
import org.kanelucky.server.log.ServerStartupLog
import org.kanelucky.server.network.NetworkRegistry
import org.kanelucky.server.terminal.ServerTerminalConsole
import org.kanelucky.server.world.blocks.WorldBlockRegistry
import org.kanelucky.server.world.generator.NormalGenerator
import org.kanelucky.server.world.fluid.FluidEventHandler
import org.kanelucky.server.world.generator.OverworldGenerator
import org.kanelucky.server.world.generator.SuperFlatGenerator.SuperFlatGenerator
import org.kanelucky.server.world.generator.SuperFlatGenerator.preset.BottomlessPit
import org.kanelucky.server.world.generator.SuperFlatGenerator.preset.ClassicFlat
import org.kanelucky.server.world.generator.SuperFlatGenerator.preset.SnowyKingdom
import org.kanelucky.server.world.generator.SuperFlatGenerator.preset.Void

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

        val dashboard = Dashboard.getInstance()

        minecraftServer = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()

        instanceContainer = instanceManager.createInstanceContainer()

        // Loading world
        Files.createDirectories(Path.of("worlds"))
        val polarPath = Path.of("worlds/world.polar")
        instanceContainer.setChunkLoader(PolarLoader(polarPath))

        // Set the ChunkGenerator
        val generator = when (ConfigManager.worldSettings.worldType) {
            WorldType.NORMAL -> OverworldGenerator()
            WorldType.SUPERFLAT -> {
                val preset = when (ConfigManager.worldSettings.superflat.preset) {
                    "classic" -> ClassicFlat()
                    "bottomless_pit" -> BottomlessPit()
                    "snowy_kingdom" -> SnowyKingdom()
                    "void" -> Void()
                    else -> ClassicFlat()
                }
                SuperFlatGenerator(preset)
            }
        }
        instanceContainer.setGenerator(generator)

        instanceContainer.setChunkSupplier(::LightingChunk)

        globalEventHandler = MinecraftServer.getGlobalEventHandler()

        WorldBlockRegistry.initialize()

        // Register events
        EventRegistry.register(handler = globalEventHandler)

        MinestomFluids.init()
        FluidEventHandler.register(globalEventHandler)
        MinecraftServer.getGlobalEventHandler().addChild(MinestomFluids.events())

        // Register commands
        CommandRegistry.initialize()

        // MinestomPvP event
        MinestomPvP.init()
        val modern = CombatFeatures.modernVanilla()
        globalEventHandler.addChild(modern.createNode())

        // Network
        NetworkRegistry.initialize()

        // Saving world
        instanceContainer.saveChunksToStorage()

        val commandManager = MinecraftServer.getCommandManager()
        val dispatcher = commandManager.dispatcher
        val console = commandManager.consoleSender

        ServerTerminalConsole().startConsole(dispatcher, console)

        minecraftServer.start(serverSettings.address, serverSettings.port)

        dashboard.afterServerStarted()

        ServerStartupLog.print()
        ServerStartupLog.done()
    }
}