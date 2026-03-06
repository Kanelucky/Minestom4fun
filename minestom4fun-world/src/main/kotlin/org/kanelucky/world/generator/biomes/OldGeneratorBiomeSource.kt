package org.kanelucky.world.generator.biomes

import org.kanelucky.world.generator.noise.OldGeneratorLayer

/**
 * Old biome source for legacy world generation
 *
 * Originally developed in OldConsoleWorldgen(https://github.com/zernix2077/OldConsoleWorldgen)
 *
 * Ported to Kotlin by Kanelucky
 */
internal class OldGeneratorBiomeSource(seed: Long, largeBiomes: Boolean) {
    private val layer: OldGeneratorLayer
    private val zoomedLayer: OldGeneratorLayer

    init {
        val layers = OldGeneratorLayer.createDefaultLayers(seed, largeBiomes)
        layer = layers[0]
        zoomedLayer = layers[1]
    }

    fun getRawBiomeBlock(x: Int, z: Int, w: Int, h: Int): Array<OldGeneratorBiome> {
        val ids = layer.getArea(x, z, w, h)
        return Array(w * h) { OldGeneratorBiome.byId(ids[it]) }
    }

    fun getBiomeBlock(x: Int, z: Int, w: Int, h: Int): Array<OldGeneratorBiome> {
        val ids = zoomedLayer.getArea(x, z, w, h)
        return Array(w * h) { OldGeneratorBiome.byId(ids[it]) }
    }

    fun getBiome(x: Int, z: Int): OldGeneratorBiome =
        OldGeneratorBiome.byId(zoomedLayer.getArea(x, z, 1, 1)[0])
}