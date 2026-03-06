package org.kanelucky.world.generator.old

import org.kanelucky.world.generator.OldGeneratorConfig
import org.kanelucky.world.generator.blocks.OldGeneratorBlockIds
import org.kanelucky.world.generator.carver.OldGeneratorCanyonCarver
import org.kanelucky.world.generator.carver.OldGeneratorCaveCarver
import org.kanelucky.world.generator.chunks.OldGeneratorChunkBuffer
import org.kanelucky.world.generator.biomes.OldGeneratorBiome
import org.kanelucky.world.generator.biomes.OldGeneratorBiomeSource
import org.kanelucky.world.generator.noise.OldGeneratorNoise
import org.kanelucky.world.generator.noise.OldGeneratorPerlinNoise

/**
 * Originally developed in OldConsoleWorldgen (https://github.com/zernix2077/OldConsoleWorldgen)
 * Ported to Kotlin/Minestom by Kanelucky
 */
internal class OldGeneratorLevelSource(private val config: OldGeneratorConfig) {

    companion object {
        private const val CHUNK_WIDTH = 4
        private const val CHUNK_HEIGHT = 8
    }

    private val biomeSource = OldGeneratorBiomeSource(config.seed, config.largeBiomes)
    private val random = OldGeneratorNoise(config.seed)
    private val lperlinNoise1 = OldGeneratorPerlinNoise(random, 16)
    private val lperlinNoise2 = OldGeneratorPerlinNoise(random, 16)
    private val perlinNoise1 = OldGeneratorPerlinNoise(random, 8)
    private val perlinNoise3 = OldGeneratorPerlinNoise(random, 4)
    private val scaleNoise = OldGeneratorPerlinNoise(random, 10)
    private val depthNoise = OldGeneratorPerlinNoise(random, 16)
    private val caveCarver = OldGeneratorCaveCarver()
    private val canyonCarver = OldGeneratorCanyonCarver()
    private var pows: FloatArray? = null

    @Synchronized
    fun generateChunk(chunkX: Int, chunkZ: Int, buffer: OldGeneratorChunkBuffer) {
        random.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L)
        prepareHeights(chunkX, chunkZ, buffer)

        val biomes = biomeSource.getBiomeBlock(chunkX * 16, chunkZ * 16, 16, 16)
        for (z in 0 until 16) {
            for (x in 0 until 16) {
                buffer.setBiome(x, z, biomes[x + z * 16])
            }
        }

        buildSurfaces(chunkX, chunkZ, buffer)
        caveCarver.apply(config.seed, chunkX, chunkZ, buffer)
        canyonCarver.apply(config.seed, chunkX, chunkZ, buffer)
    }

    private fun prepareHeights(chunkX: Int, chunkZ: Int, buffer: OldGeneratorChunkBuffer) {
        val xChunks = 16 / CHUNK_WIDTH
        val yChunks = OldGeneratorChunkBuffer.GEN_DEPTH / CHUNK_HEIGHT
        val xSize = xChunks + 1
        val ySize = yChunks + 1
        val zSize = xChunks + 1

        val biomes = biomeSource.getRawBiomeBlock(chunkX * CHUNK_WIDTH - 2, chunkZ * CHUNK_WIDTH - 2, xSize + 5, zSize + 5)
        val heights = getHeights(chunkX * xChunks, 0, chunkZ * xChunks, xSize, ySize, zSize, biomes, xSize + 5)

        for (xc in 0 until xChunks) {
            for (zc in 0 until xChunks) {
                for (yc in 0 until yChunks) {
                    val yStep = 1.0 / CHUNK_HEIGHT
                    var s0 = heights[((xc) * zSize + zc) * ySize + yc]
                    var s1 = heights[((xc) * zSize + (zc + 1)) * ySize + yc]
                    var s2 = heights[((xc + 1) * zSize + zc) * ySize + yc]
                    var s3 = heights[((xc + 1) * zSize + (zc + 1)) * ySize + yc]
                    val s0a = (heights[((xc) * zSize + zc) * ySize + (yc + 1)] - s0) * yStep
                    val s1a = (heights[((xc) * zSize + (zc + 1)) * ySize + (yc + 1)] - s1) * yStep
                    val s2a = (heights[((xc + 1) * zSize + zc) * ySize + (yc + 1)] - s2) * yStep
                    val s3a = (heights[((xc + 1) * zSize + (zc + 1)) * ySize + (yc + 1)] - s3) * yStep

                    for (y in 0 until CHUNK_HEIGHT) {
                        val xStep = 1.0 / CHUNK_WIDTH
                        var currentS0 = s0
                        var currentS1 = s1
                        val currentS0a = (s2 - s0) * xStep
                        val currentS1a = (s3 - s1) * xStep

                        for (x in 0 until CHUNK_WIDTH) {
                            val zStep = 1.0 / CHUNK_WIDTH
                            var value = currentS0
                            val valueStep = (currentS1 - currentS0) * zStep
                            value -= valueStep
                            for (z in 0 until CHUNK_WIDTH) {
                                value += valueStep
                                val worldY = yc * CHUNK_HEIGHT + y
                                val blockId = when {
                                    value > 0.0 -> OldGeneratorBlockIds.STONE
                                    worldY < config.seaLevel -> OldGeneratorBlockIds.WATER
                                    else -> OldGeneratorBlockIds.AIR
                                }
                                buffer.setBlock(x + xc * CHUNK_WIDTH, worldY, z + zc * CHUNK_WIDTH, blockId)
                            }
                            currentS0 += currentS0a
                            currentS1 += currentS1a
                        }

                        s0 += s0a
                        s1 += s1a
                        s2 += s2a
                        s3 += s3a
                    }
                }
            }
        }
    }

    private fun buildSurfaces(chunkX: Int, chunkZ: Int, buffer: OldGeneratorChunkBuffer) {
        val scale = 1.0 / 32.0
        val depthBuffer = perlinNoise3.getRegion(null, chunkX * 16, chunkZ * 16, 16, 16, scale * 2.0, scale * 2.0)

        for (x in 0 until 16) {
            for (z in 0 until 16) {
                val biome = buffer.getBiome(x, z) ?: continue
                val temperature = biome.temperature
                val runDepth = (depthBuffer[x + z * 16] / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
                var run = -1
                var top = biome.topBlockId
                var filler = biome.fillerBlockId

                for (y in OldGeneratorChunkBuffer.GEN_DEPTH - 1 downTo 0) {
                    if (y <= 1 + random.nextInt(2)) {
                        buffer.setBlock(x, y, z, OldGeneratorBlockIds.BEDROCK)
                        continue
                    }

                    val old = buffer.getBlock(x, y, z)
                    when {
                        old == OldGeneratorBlockIds.AIR -> run = -1
                        old == OldGeneratorBlockIds.STONE -> {
                            if (run == -1) {
                                if (runDepth <= 0) {
                                    top = OldGeneratorBlockIds.AIR
                                    filler = OldGeneratorBlockIds.STONE
                                } else if (y >= config.seaLevel - 4 && y <= config.seaLevel + 1) {
                                    top = biome.topBlockId
                                    filler = biome.fillerBlockId
                                }

                                if (y < config.seaLevel && top == OldGeneratorBlockIds.AIR) {
                                    top = if (temperature < 0.15f) OldGeneratorBlockIds.ICE else OldGeneratorBlockIds.WATER
                                }

                                run = runDepth
                                buffer.setBlock(x, y, z, if (y >= config.seaLevel - 1) top else filler)
                            } else if (run > 0) {
                                run--
                                buffer.setBlock(x, y, z, filler)
                                if (run == 0 && filler == OldGeneratorBlockIds.SAND) {
                                    run = random.nextInt(4)
                                    filler = OldGeneratorBlockIds.SANDSTONE
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getHeights(x: Int, y: Int, z: Int, xSize: Int, ySize: Int, zSize: Int, biomes: Array<OldGeneratorBiome>, biomeWidth: Int): DoubleArray {
        if (pows == null) {
            pows = FloatArray(25)
            for (xb in -2..2) {
                for (zb in -2..2) {
                    pows!![xb + 2 + (zb + 2) * 5] = (10.0 / Math.sqrt(xb * xb + zb * zb + 0.2)).toFloat()
                }
            }
        }
        val powsArr = pows!!

        val s = 684.412
        val hs = 684.412
        scaleNoise.getRegion(null, x, z, xSize, zSize, 1.121, 1.121)
        val dr = depthNoise.getRegion(null, x, z, xSize, zSize, 200.0, 200.0)
        val pnr = perlinNoise1.getRegion(null, x, y, z, xSize, ySize, zSize, s / 80.0, hs / 160.0, s / 80.0)
        val ar = lperlinNoise1.getRegion(null, x, y, z, xSize, ySize, zSize, s, hs, s)
        val br = lperlinNoise2.getRegion(null, x, y, z, xSize, ySize, zSize, s, hs, s)
        val buffer = DoubleArray(xSize * ySize * zSize)

        var p = 0
        var pp = 0
        for (xx in 0 until xSize) {
            for (zz in 0 until zSize) {
                var weightedScale = 0.0f
                var weightedDepth = 0.0f
                var weightTotal = 0.0f
                val middleBiome = biomes[(xx + 2) + (zz + 2) * biomeWidth]

                for (xb in -2..2) {
                    for (zb in -2..2) {
                        val biome = biomes[(xx + xb + 2) + (zz + zb + 2) * biomeWidth]
                        var weight = powsArr[xb + 2 + (zb + 2) * 5] / (biome.depth + 2.0f)
                        if (biome.depth > middleBiome.depth) weight /= 2.0f
                        weightedScale += biome.scale * weight
                        weightedDepth += biome.depth * weight
                        weightTotal += weight
                    }
                }

                weightedScale /= weightTotal
                weightedDepth /= weightTotal
                weightedScale = weightedScale * 0.9f + 0.1f
                weightedDepth = (weightedDepth * 4.0f - 1.0f) / 8.0f

                var randomDepth = dr[pp] / 8000.0
                if (randomDepth < 0.0) randomDepth = -randomDepth * 0.3
                randomDepth = randomDepth * 3.0 - 2.0
                if (randomDepth < 0.0) {
                    randomDepth /= 2.0
                    if (randomDepth < -1.0) randomDepth = -1.0
                    randomDepth /= 1.4
                    randomDepth /= 2.0
                } else {
                    if (randomDepth > 1.0) randomDepth = 1.0
                    randomDepth /= 8.0
                }
                pp++

                for (yy in 0 until ySize) {
                    var depth = weightedDepth + randomDepth * 0.2
                    val scaleValue = weightedScale.toDouble()
                    depth = depth * ySize / 16.0
                    val yCenter = ySize / 2.0 + depth * 4.0
                    var yOffset = (yy - yCenter) * 12.0 * 128.0 / OldGeneratorChunkBuffer.GEN_DEPTH / scaleValue
                    if (yOffset < 0.0) yOffset *= 4.0

                    val low = ar[p] / 512.0
                    val high = br[p] / 512.0
                    val blend = (pnr[p] / 10.0 + 1.0) / 2.0
                    var value = when {
                        blend < 0.0 -> low
                        blend > 1.0 -> high
                        else -> low + (high - low) * blend
                    }
                    value -= yOffset

                    if (yy > ySize - 4) {
                        val slide = (yy - (ySize - 4)) / 3.0
                        value = value * (1.0 - slide) + -10.0 * slide
                    }

                    buffer[p] = value
                    p++
                }
            }
        }

        return buffer
    }
}