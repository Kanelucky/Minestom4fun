package org.kanelucky.entity.ai.sensor

import net.minestom.server.entity.EntityCreature

interface Sensor {
    val period: Int get() = 20
    fun sense(entity: EntityCreature)
}