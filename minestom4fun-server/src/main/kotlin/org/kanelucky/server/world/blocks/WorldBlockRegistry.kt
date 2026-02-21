package org.kanelucky.server.world.blocks

import org.kanelucky.server.world.blocks.handler.BlockDropHandler
import org.kanelucky.server.world.blocks.handler.FragileBlockHandler
import org.kanelucky.server.world.blocks.handler.GrassDropHandler

/**
 * @author Kanelucky
 */
object WorldBlockRegistry {

    fun initialize() {
        FragileBlockHandler.register()
        BlockDropHandler.register()
        GrassDropHandler.register()
    }
}