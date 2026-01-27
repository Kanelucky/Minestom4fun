package org.kanelucky

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer

import io.github.togar2.pvp.MinestomPvP
import io.github.togar2.pvp.feature.CombatFeatures

import org.kanelucky.commands.CommandRegistry
import org.kanelucky.event.EventRegistry
import org.kanelucky.world.generator.OverworldGenerator


object Minestom4fun {

    lateinit var instanceContainer: InstanceContainer
    lateinit var globalEventHandler: GlobalEventHandler
    lateinit var minecraftServer: MinecraftServer

    @JvmStatic
    fun main() {
        // Initialization
        minecraftServer = MinecraftServer.init()

        val instanceManager = MinecraftServer.getInstanceManager()
        instanceContainer = instanceManager.createInstanceContainer()

        instanceContainer.setGenerator(OverworldGenerator())
        globalEventHandler = MinecraftServer.getGlobalEventHandler()

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            val player = event.getPlayer()
            event.spawningInstance = instanceContainer
            player.respawnPoint = Pos(0.0, 42.0, 0.0)
        }

        // Register events
        EventRegistry.register(handler = globalEventHandler)

        // Register commands
        CommandRegistry.initialize()

        MinestomPvP.init()

        val modernVanilla = CombatFeatures.modernVanilla()
        MinecraftServer.getGlobalEventHandler().addChild(modernVanilla.createNode())

        minecraftServer.start("0.0.0.0", 25565)
    }
}