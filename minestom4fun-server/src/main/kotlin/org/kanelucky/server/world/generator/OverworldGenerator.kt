package org.kanelucky.server.world.generator

import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.instance.generator.Generator

import org.kanelucky.server.config.ConfigManager
import org.kanelucky.server.world.generator.features.decoration.SeagrassDecorator
import org.kanelucky.server.world.generator.features.decoration.VegetationDecorator
import org.kanelucky.server.world.generator.noise.FastNoise
import org.kanelucky.server.world.generator.terrain.BaseTerrain
import org.kanelucky.server.world.generator.terrain.CaveCarver
import org.kanelucky.server.world.generator.terrain.RiverCarver

/**
 * Overworld terrain generator
 *
 * This generator aims to reproduce vanilla-like terrain
 * behavior as closely as possible, including terrain
 * shaping, rivers, caves, and natural decoration
 *
 * Status: EXPERIMENTAL / UNSTABLE
 *
 * Compared to NormalGenerator, this version prioritizes
 * visual authenticity over stability and may still
 * produce artifacts, edge cases, or performance issues
 *
 * @author Kanelucky
 */
class OverworldGenerator : Generator {

    private val terrain = BaseTerrain()
    private val caves = CaveCarver()
    private val riverNoise = FastNoise(ConfigManager.serverSettings.seed)
    private val rivers = RiverCarver(riverNoise)
    private val vegetation = VegetationDecorator()
    private val seagrass = SeagrassDecorator()


    override fun generate(unit: GenerationUnit) {
        val start = unit.absoluteStart()
        val baseX = start.blockX()
        val baseZ = start.blockZ()
        val heights = terrain.build(unit)

        caves.carve(unit, heights, baseX, baseZ)
        rivers.carve(unit, heights, baseX, baseZ)
        seagrass.decorate(unit, heights, baseX, baseZ)
        vegetation.decorate(unit, heights, baseX, baseZ)
    }
}