package org.kanelucky.server.events.server

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

import org.kanelucky.server.Minestom4fun
import org.kanelucky.server.player.data.PlayerDataManager

/**
 * @author Kanelucky
 */
object PlayerSpawnEvent {
    @JvmStatic
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
                event.spawningInstance = Minestom4fun.instanceContainer
            }

        MinecraftServer.getGlobalEventHandler()
            .addListener(net.minestom.server.event.player.PlayerSpawnEvent::class.java) { event ->
                if (!event.isFirstSpawn) return@addListener

                val player = event.player
                val instance = Minestom4fun.instanceContainer
                val chunk = instance.loadChunk(0, 0).join()
                val y = chunk.motionBlockingHeightmap().getHeight(0, 0)

                player.teleport(Pos(0.5, y + 1.0, 0.5))
                PlayerDataManager.load(player)
                PlayerDataManager.save(player)
            }
    }
}