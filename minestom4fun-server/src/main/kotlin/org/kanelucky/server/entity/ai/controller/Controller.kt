package org.kanelucky.server.entity.ai.controller

import net.minestom.server.entity.EntityCreature

interface Controller {
    fun control(entity: EntityCreature)
}