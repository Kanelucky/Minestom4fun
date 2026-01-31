package org.kanelucky.server.event.player

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.heightmap.WorldSurfaceHeightmap

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

                val chunk = instance.getChunkAt(0.0, 0.0) ?: return@addListener

                val heightmap = WorldSurfaceHeightmap(chunk)
                val y = heightmap.getHeight(0, 0)

                event.player.respawnPoint = Pos(0.0, y.toDouble() + 1, 0.0)

            }
    }
}