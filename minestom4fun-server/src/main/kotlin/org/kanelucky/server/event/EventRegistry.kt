package org.kanelucky.server.event

import net.minestom.server.event.GlobalEventHandler

/**
 * @author Kanelucky
 */
object EventRegistry {
    fun register(handler: GlobalEventHandler) {
        GlobalEventsRegistry.register(handler)
        EventNodesRegistry.register(handler)
    }
}