package org.kanelucky.server.network.status

import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent

import org.kanelucky.server.config.ConfigManager

/**
 * @author Kanelucky
 */
object ServerBrand {

    private val BRAND = ConfigManager.networkSettings.serverBrand

    fun register() {
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            event.player.sendPluginMessage("minecraft:brand", writeBrand(BRAND))
        }
    }

    private fun writeBrand(brand: String): ByteArray {
        val bytes = brand.toByteArray()
        val result = ByteArray(bytes.size + 1)
        result[0] = bytes.size.toByte()
        System.arraycopy(bytes, 0, result, 1, bytes.size)
        return result
    }
}