package org.kanelucky.server.log

import net.minestom.server.MinecraftServer
import org.kanelucky.server.config.ConfigManager

/**
 * @author Kanelucky
 */
object ServerStartupLog {

    private val startTime = System.currentTimeMillis()

    fun print() {
        val logger = MinecraftServer.LOGGER
        val cfg = ConfigManager.serverSettings
        val net = ConfigManager.networkSettings

        logger.info("Starting ${net.serverBrand} server")
        logger.info("Default game type: SURVIVAL")
        logger.info("Starting Minecraft server on ${cfg.address}:${cfg.port}")
        logger.info("Preparing level \"world\"")
    }

    fun done() {
        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
        MinecraftServer.LOGGER.info("Done (${String.format("%.3f", elapsed)}s)! For help, type \"help\"")
    }
}