package org.kanelucky.world.generator.SuperFlatGenerator

import net.minestom.server.instance.block.Block

/**
 * @author Kanelucky
 */
data class Layer(
    val fromY: Int,
    val toY: Int,
    val block: Block,
)
