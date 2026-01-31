package org.kanelucky.world.generator.SuperFlatGenerator

import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator

/**
 * @author Kanelucky
 */
class SuperFlatGenerator(
    private val preset: SuperFlatPreset
) : Generator {

    override fun generate(unit: GenerationUnit) {
        for (layer in preset.layers) {
            unit.modifier().fillHeight(
                layer.fromY,
                layer.toY,
                layer.block
            )
        }
    }
}