package org.kanelucky.world.generator.noise

import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise

/**
 * @author Kanelucky
 */
object NoiseBuilder {

    fun continental(seed: Long) = build(seed, 0.002)
    fun erosion(seed: Long) = build(seed + 1, 0.003)
    fun peaksAndValleys(seed: Long) = build(seed + 2, 0.004)
    fun blending(seed: Long) = build(seed + 3, 0.001)
    fun cave(seed: Long) = build(seed + 4, 0.008)
    fun noodleCave(seed: Long) = build(seed + 5, 0.01)
    fun aquifer(seed: Long) = build(seed + 6, 0.003)
    fun river(seed: Long) = build(seed + 8, 0.004)
    fun bedrock(seed: Long) = build(seed + 9, 0.5)
    fun deepslate(seed: Long) = build(seed + 10, 0.1)

    private fun build(seed: Long, scale: Double) = JNoise.newBuilder()
        .superSimplex(SuperSimplexNoiseGenerator.newBuilder().setSeed(seed).build())
        .scale(scale)
        .build()
}