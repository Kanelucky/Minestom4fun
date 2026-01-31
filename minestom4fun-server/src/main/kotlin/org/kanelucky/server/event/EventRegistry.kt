package org.kanelucky.event

import net.minestom.server.event.GlobalEventHandler

/**
 * @author Kanelucky
 */
object EventRegistry {
    fun register(handler: GlobalEventHandler) {
        GlobalEvents.register(handler)
        EventNodes.register(handler)
    }
}