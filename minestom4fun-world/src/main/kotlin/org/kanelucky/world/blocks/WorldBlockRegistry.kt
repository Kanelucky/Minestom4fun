package org.kanelucky.world.blocks

import org.kanelucky.world.blocks.handler.BlockDropHandler
import org.kanelucky.world.blocks.handler.FragileBlockHandler
import org.kanelucky.world.blocks.handler.GrassDropHandler

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