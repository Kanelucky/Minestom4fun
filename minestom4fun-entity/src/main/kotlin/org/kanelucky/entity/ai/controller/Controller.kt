package org.kanelucky.entity.ai.controller

import net.minestom.server.entity.EntityCreature

interface Controller {
    fun control(entity: EntityCreature)
}