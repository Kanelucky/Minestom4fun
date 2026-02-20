package org.kanelucky.server.events.player

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

import org.kanelucky.server.Minestom4fun.instanceContainer

/**
 * @author Kanelucky
 */
object PlayerSpawnEvent {
    @JvmStatic
    fun register() {
        MinecraftServer.getGlobalEventHandler()
            .addListener(AsyncPlayerConfigurationEvent::class.java) { event ->

                val instance = instanceContainer
                event.spawningInstance = instance

                val chunk = instance.loadChunk(0, 0).join()
                val y = chunk.motionBlockingHeightmap().getHeight(0, 0)

                event.player.respawnPoint = Pos(0.0, y + 1.0, 0.0)

            }
    }
}