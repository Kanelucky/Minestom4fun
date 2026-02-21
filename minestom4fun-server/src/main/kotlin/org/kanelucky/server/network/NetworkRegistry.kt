package org.kanelucky.server.network

import org.kanelucky.server.network.status.ServerBrand
import org.kanelucky.server.network.status.ServerListPing

/**
 * @author Kanelucky
 */
object NetworkRegistry {

    fun initialize() {
        ServerBrand.register()
        ServerListPing.register()
    }
}