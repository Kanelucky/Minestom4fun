package org.kanelucky.world.generator.noise

import org.kanelucky.world.generator.biomes.OldGeneratorBiome

/**
 * Old layer-based biome generation system
 *
 * Originally developed in OldConsoleWorldgen(https://github.com/zernix2077/OldConsoleWorldgen)
 *
 * Ported to Kotlin by Kanelucky
 */
internal abstract class OldGeneratorLayer(seedMixup: Long) {
    private val seedMixup: Long
    protected var parent: OldGeneratorLayer? = null
    private var seed: Long = 0
    private var randomValue: Long = 0

    init {
        var value = seedMixup
        value = mix(value, seedMixup)
        value = mix(value, seedMixup)
        value = mix(value, seedMixup)
        this.seedMixup = value
    }

    open fun init(seed: Long) {
        this.seed = seed
        parent?.init(seed)
        this.seed = mix(this.seed, seedMixup)
        this.seed = mix(this.seed, seedMixup)
        this.seed = mix(this.seed, seedMixup)
    }

    protected fun initRandom(x: Long, y: Long) {
        randomValue = seed
        randomValue = mix(randomValue, x)
        randomValue = mix(randomValue, y)
        randomValue = mix(randomValue, x)
        randomValue = mix(randomValue, y)
    }

    protected fun nextRandom(max: Int): Int {
        var result = ((randomValue shr 24) % max).toInt()
        if (result < 0) result += max
        randomValue = mix(randomValue, seed)
        return result
    }

    abstract fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray

    companion object {
        fun createDefaultLayers(seed: Long, largeBiomes: Boolean): Array<OldGeneratorLayer> {
            var islandLayer: OldGeneratorLayer = IslandLayer(1L)
            islandLayer = FuzzyZoomLayer(2000L, islandLayer)
            islandLayer = AddIslandLayer(1L, islandLayer)
            islandLayer = ZoomLayer(2001L, islandLayer)
            islandLayer = AddIslandLayer(2L, islandLayer)
            islandLayer = AddSnowLayer(2L, islandLayer)
            islandLayer = ZoomLayer(2002L, islandLayer)
            islandLayer = AddIslandLayer(3L, islandLayer)
            islandLayer = ZoomLayer(2003L, islandLayer)
            islandLayer = AddIslandLayer(4L, islandLayer)

            val zoomLevel = if (largeBiomes) 6 else 4

            var riverLayer: OldGeneratorLayer = islandLayer
            riverLayer = ZoomLayer.zoom(1000L, riverLayer, 0)
            riverLayer = RiverInitLayer(100L, riverLayer)
            riverLayer = ZoomLayer.zoom(1000L, riverLayer, zoomLevel + 2)
            riverLayer = RiverLayer(1L, riverLayer)
            riverLayer = SmoothLayer(1000L, riverLayer)

            var biomeLayer: OldGeneratorLayer = islandLayer
            biomeLayer = ZoomLayer.zoom(1000L, biomeLayer, 0)
            biomeLayer = BiomeInitLayer(200L, biomeLayer, false)
            biomeLayer = ZoomLayer.zoom(1000L, biomeLayer, 2)
            biomeLayer = RegionHillsLayer(1000L, biomeLayer)

            for (i in 0 until zoomLevel) {
                biomeLayer = ZoomLayer(1000L + i, biomeLayer)
                if (i == 0) {
                    biomeLayer = AddIslandLayer(3L, biomeLayer)
                    biomeLayer = AddMushroomIslandLayer(5L, biomeLayer)
                }
                if (i == 1) {
                    biomeLayer = GrowMushroomIslandLayer(5L, biomeLayer)
                    biomeLayer = ShoreLayer(1000L, biomeLayer)
                    biomeLayer = SwampRiversLayer(1000L, biomeLayer)
                }
            }

            biomeLayer = SmoothLayer(1000L, biomeLayer)
            biomeLayer = RiverMixerLayer(100L, biomeLayer, riverLayer)

            val zoomedLayer = VoronoiZoomLayer(10L, biomeLayer)
            biomeLayer.init(seed)
            zoomedLayer.init(seed)
            return arrayOf(biomeLayer, zoomedLayer)
        }

        private fun mix(seed: Long, value: Long): Long {
            var s = seed * seed * 6364136223846793005L + 1442695040888963407L
            s += value
            return s
        }
    }
}

internal class IslandLayer(seedMixup: Long) : OldGeneratorLayer(seedMixup) {
    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                initRandom((xo + x).toLong(), (yo + y).toLong())
                result[x + y * w] = if (nextRandom(10) == 0) 1 else 0
            }
        }
        if (xo > -w && xo <= 0 && yo > -h && yo <= 0) {
            result[-xo + (-yo * w)] = 1
        }
        return result
    }
}

internal abstract class AbstractZoomLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    protected fun zoomArea(xo: Int, yo: Int, w: Int, h: Int, fuzzy: Boolean): IntArray {
        val px = xo shr 1
        val py = yo shr 1
        val pw = (w shr 1) + 3
        val ph = (h shr 1) + 3
        val parentArea = parent!!.getArea(px, py, pw, ph)

        val tmpWidth = pw shl 1
        val tmp = IntArray((pw * 2) * (ph * 2))

        for (y in 0 until ph - 1) {
            val row = y shl 1
            var index = row * tmpWidth
            var ul = parentArea[y * pw]
            var dl = parentArea[(y + 1) * pw]
            for (x in 0 until pw - 1) {
                initRandom((x + px).toLong() shl 1, (y + py).toLong() shl 1)
                val ur = parentArea[(x + 1) + y * pw]
                val dr = parentArea[(x + 1) + (y + 1) * pw]
                tmp[index] = ul
                tmp[index++ + tmpWidth] = choose(ul, dl)
                tmp[index] = choose(ul, ur)
                tmp[index++ + tmpWidth] = if (fuzzy) chooseFuzzy(ul, ur, dl, dr) else chooseModeOrRandom(ul, ur, dl, dr)
                ul = ur
                dl = dr
            }
        }

        val result = IntArray(w * h)
        for (y in 0 until h) {
            System.arraycopy(tmp, (y + (yo and 1)) * (pw shl 1) + (xo and 1), result, y * w, w)
        }
        return result
    }

    private fun choose(a: Int, b: Int) = if (nextRandom(2) == 0) a else b

    private fun chooseFuzzy(a: Int, b: Int, c: Int, d: Int) = when (nextRandom(4)) {
        0 -> a; 1 -> b; 2 -> c; else -> d
    }

    private fun chooseModeOrRandom(a: Int, b: Int, c: Int, d: Int): Int {
        if (b == c && c == d) return b
        if (a == b && a == c) return a
        if (a == b && a == d) return a
        if (a == c && a == d) return a
        if (a == b && c != d) return a
        if (a == c && b != d) return a
        if (a == d && b != c) return a
        if (b == c && a != d) return b
        if (b == d && a != c) return b
        if (c == d && a != b) return c
        return chooseFuzzy(a, b, c, d)
    }
}

internal class FuzzyZoomLayer(seedMixup: Long, parent: OldGeneratorLayer) : AbstractZoomLayer(seedMixup, parent) {
    override fun getArea(xo: Int, yo: Int, w: Int, h: Int) = zoomArea(xo, yo, w, h, true)
}

internal class ZoomLayer(seedMixup: Long, parent: OldGeneratorLayer) : AbstractZoomLayer(seedMixup, parent) {
    override fun getArea(xo: Int, yo: Int, w: Int, h: Int) = zoomArea(xo, yo, w, h, false)

    companion object {
        fun zoom(seed: Long, parent: OldGeneratorLayer, count: Int): OldGeneratorLayer {
            var result = parent
            for (i in 0 until count) result = ZoomLayer(seed + i, result)
            return result
        }
    }
}

internal class AddIslandLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val parentArea = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val n1 = parentArea[x + y * (w + 2)]
                val n2 = parentArea[(x + 2) + y * (w + 2)]
                val n3 = parentArea[x + (y + 2) * (w + 2)]
                val n4 = parentArea[(x + 2) + (y + 2) * (w + 2)]
                val center = parentArea[(x + 1) + (y + 1) * (w + 2)]
                initRandom((x + xo).toLong(), (y + yo).toLong())

                if (center == 0 && (n1 != 0 || n2 != 0 || n3 != 0 || n4 != 0)) {
                    var odds = 1; var swap = 1
                    if (n1 != 0 && nextRandom(odds++) == 0) swap = n1
                    if (n2 != 0 && nextRandom(odds++) == 0) swap = n2
                    if (n3 != 0 && nextRandom(odds++) == 0) swap = n3
                    if (n4 != 0 && nextRandom(odds++) == 0) swap = n4
                    result[x + y * w] = if (nextRandom(3) == 0) swap
                    else if (swap == OldGeneratorBiome.ICE_FLATS.id) OldGeneratorBiome.FROZEN_OCEAN.id else 0
                } else if (center > 0 && (n1 == 0 || n2 == 0 || n3 == 0 || n4 == 0)) {
                    result[x + y * w] = if (nextRandom(5) == 0)
                        (if (center == OldGeneratorBiome.ICE_FLATS.id) OldGeneratorBiome.FROZEN_OCEAN.id else 0)
                    else center
                } else {
                    result[x + y * w] = center
                }
            }
        }
        return result
    }
}

internal class AddSnowLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val parentArea = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val center = parentArea[(x + 1) + (y + 1) * (w + 2)]
                initRandom((x + xo).toLong(), (y + yo).toLong())
                result[x + y * w] = if (center == 0) 0
                else if (nextRandom(5) == 0) OldGeneratorBiome.ICE_FLATS.id else 1
            }
        }
        return result
    }
}

internal class SmoothLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val parentArea = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val left   = parentArea[x + (y + 1) * (w + 2)]
                val right  = parentArea[(x + 2) + (y + 1) * (w + 2)]
                val up     = parentArea[(x + 1) + y * (w + 2)]
                val down   = parentArea[(x + 1) + (y + 2) * (w + 2)]
                var center = parentArea[(x + 1) + (y + 1) * (w + 2)]
                if (left == right && up == down) {
                    initRandom((x + xo).toLong(), (y + yo).toLong())
                    center = if (nextRandom(2) == 0) left else up
                } else {
                    if (left == right) center = left
                    if (up == down) center = up
                }
                result[x + y * w] = center
            }
        }
        return result
    }
}

internal class RiverInitLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val base = parent!!.getArea(xo, yo, w, h)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                initRandom((x + xo).toLong(), (y + yo).toLong())
                result[x + y * w] = if (base[x + y * w] > 0) nextRandom(2) + 2 else 0
            }
        }
        return result
    }
}

internal class RiverLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val parentArea = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val left   = parentArea[x + (y + 1) * (w + 2)]
                val right  = parentArea[(x + 2) + (y + 1) * (w + 2)]
                val up     = parentArea[(x + 1) + y * (w + 2)]
                val down   = parentArea[(x + 1) + (y + 2) * (w + 2)]
                val center = parentArea[(x + 1) + (y + 1) * (w + 2)]
                result[x + y * w] = if (center == 0 || left == 0 || right == 0 || up == 0 || down == 0
                    || center != left || center != right || center != up || center != down)
                    OldGeneratorBiome.RIVER.id else -1
            }
        }
        return result
    }
}

internal class BiomeInitLayer(seedMixup: Long, parent: OldGeneratorLayer, legacy11: Boolean) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    private val startBiomes = if (legacy11)
        arrayOf(OldGeneratorBiome.DESERT, OldGeneratorBiome.FOREST, OldGeneratorBiome.EXTREME_HILLS, OldGeneratorBiome.SWAMPLAND,
            OldGeneratorBiome.PLAINS, OldGeneratorBiome.TAIGA)
    else
        arrayOf(OldGeneratorBiome.DESERT, OldGeneratorBiome.FOREST, OldGeneratorBiome.EXTREME_HILLS, OldGeneratorBiome.SWAMPLAND,
            OldGeneratorBiome.PLAINS, OldGeneratorBiome.TAIGA, OldGeneratorBiome.JUNGLE)

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val base = parent!!.getArea(xo, yo, w, h)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                initRandom((x + xo).toLong(), (y + yo).toLong())
                val old = base[x + y * w]
                result[x + y * w] = when {
                    old == 0 -> 0
                    old == OldGeneratorBiome.MUSHROOM_ISLAND.id -> old
                    old == 1 -> startBiomes[nextRandom(startBiomes.size)].id
                    else -> {
                        val pick = startBiomes[nextRandom(startBiomes.size)].id
                        if (pick == OldGeneratorBiome.TAIGA.id) pick else OldGeneratorBiome.ICE_FLATS.id
                    }
                }
            }
        }
        return result
    }
}

internal class RegionHillsLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val base = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                initRandom((x + xo).toLong(), (y + yo).toLong())
                val old = base[(x + 1) + (y + 1) * (w + 2)]
                if (nextRandom(3) == 0) {
                    val next = when (old) {
                        2  -> OldGeneratorBiome.DESERT_HILLS.id
                        4  -> OldGeneratorBiome.FOREST_HILLS.id
                        5  -> OldGeneratorBiome.TAIGA_HILLS.id
                        1  -> OldGeneratorBiome.FOREST.id
                        12 -> OldGeneratorBiome.ICE_MOUNTAINS.id
                        21 -> OldGeneratorBiome.JUNGLE_HILLS.id
                        else -> old
                    }
                    if (next != old) {
                        val north = base[(x + 1) + y * (w + 2)]
                        val east  = base[(x + 2) + (y + 1) * (w + 2)]
                        val west  = base[x + (y + 1) * (w + 2)]
                        val south = base[(x + 1) + (y + 2) * (w + 2)]
                        result[x + y * w] = if (north == old && east == old && west == old && south == old) next else old
                    } else {
                        result[x + y * w] = old
                    }
                } else {
                    result[x + y * w] = old
                }
            }
        }
        return result
    }
}

internal class AddMushroomIslandLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val parentArea = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val n1     = parentArea[x + y * (w + 2)]
                val n2     = parentArea[(x + 2) + y * (w + 2)]
                val n3     = parentArea[x + (y + 2) * (w + 2)]
                val n4     = parentArea[(x + 2) + (y + 2) * (w + 2)]
                val center = parentArea[(x + 1) + (y + 1) * (w + 2)]
                initRandom((x + xo).toLong(), (y + yo).toLong())
                result[x + y * w] = if (center == 0 && n1 == 0 && n2 == 0 && n3 == 0 && n4 == 0 && nextRandom(100) == 0)
                    OldGeneratorBiome.MUSHROOM_ISLAND.id else center
            }
        }
        return result
    }
}

internal class GrowMushroomIslandLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val parentArea = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val n1     = parentArea[x + y * (w + 2)]
                val n2     = parentArea[(x + 2) + y * (w + 2)]
                val n3     = parentArea[x + (y + 2) * (w + 2)]
                val n4     = parentArea[(x + 2) + (y + 2) * (w + 2)]
                val center = parentArea[(x + 1) + (y + 1) * (w + 2)]
                result[x + y * w] = if (n1 == OldGeneratorBiome.MUSHROOM_ISLAND.id || n2 == OldGeneratorBiome.MUSHROOM_ISLAND.id
                    || n3 == OldGeneratorBiome.MUSHROOM_ISLAND.id || n4 == OldGeneratorBiome.MUSHROOM_ISLAND.id)
                    OldGeneratorBiome.MUSHROOM_ISLAND.id else center
            }
        }
        return result
    }
}

internal class ShoreLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val base = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val old   = base[(x + 1) + (y + 1) * (w + 2)]
                val north = base[(x + 1) + y * (w + 2)]
                val east  = base[(x + 2) + (y + 1) * (w + 2)]
                val west  = base[x + (y + 1) * (w + 2)]
                val south = base[(x + 1) + (y + 2) * (w + 2)]
                result[x + y * w] = when {
                    old == OldGeneratorBiome.MUSHROOM_ISLAND.id ->
                        if (north == OldGeneratorBiome.OCEAN.id || east == OldGeneratorBiome.OCEAN.id || west == OldGeneratorBiome.OCEAN.id || south == OldGeneratorBiome.OCEAN.id)
                            OldGeneratorBiome.MUSHROOM_ISLAND_SHORE.id else old
                    old != OldGeneratorBiome.OCEAN.id && old != OldGeneratorBiome.RIVER.id && old != OldGeneratorBiome.SWAMPLAND.id && old != OldGeneratorBiome.EXTREME_HILLS.id ->
                        if (north == OldGeneratorBiome.OCEAN.id || east == OldGeneratorBiome.OCEAN.id || west == OldGeneratorBiome.OCEAN.id || south == OldGeneratorBiome.OCEAN.id)
                            OldGeneratorBiome.BEACH.id else old
                    old == OldGeneratorBiome.EXTREME_HILLS.id ->
                        if (north != OldGeneratorBiome.EXTREME_HILLS.id || east != OldGeneratorBiome.EXTREME_HILLS.id || west != OldGeneratorBiome.EXTREME_HILLS.id || south != OldGeneratorBiome.EXTREME_HILLS.id)
                            OldGeneratorBiome.EXTREME_HILLS_EDGE.id else old
                    else -> old
                }
            }
        }
        return result
    }
}

internal class SwampRiversLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val base = parent!!.getArea(xo - 1, yo - 1, w + 2, h + 2)
        val result = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                initRandom((x + xo).toLong(), (y + yo).toLong())
                val old = base[(x + 1) + (y + 1) * (w + 2)]
                result[x + y * w] = if ((old == OldGeneratorBiome.SWAMPLAND.id && nextRandom(6) == 0)
                    || ((old == OldGeneratorBiome.JUNGLE.id || old == OldGeneratorBiome.JUNGLE_HILLS.id) && nextRandom(8) == 0))
                    OldGeneratorBiome.RIVER.id else old
            }
        }
        return result
    }
}

internal class RiverMixerLayer(seedMixup: Long, biomes: OldGeneratorLayer, rivers: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    private val biomes = biomes
    private val rivers = rivers

    override fun init(seed: Long) {
        biomes.init(seed)
        rivers.init(seed)
        super.init(seed)
    }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val biomeArea = biomes.getArea(xo, yo, w, h)
        val riverArea = rivers.getArea(xo, yo, w, h)
        val result = IntArray(w * h)
        for (i in result.indices) {
            result[i] = when {
                biomeArea[i] == OldGeneratorBiome.OCEAN.id -> biomeArea[i]
                riverArea[i] >= 0 -> when (biomeArea[i]) {
                    OldGeneratorBiome.ICE_FLATS.id -> OldGeneratorBiome.FROZEN_RIVER.id
                    OldGeneratorBiome.MUSHROOM_ISLAND.id, OldGeneratorBiome.MUSHROOM_ISLAND_SHORE.id -> OldGeneratorBiome.MUSHROOM_ISLAND.id
                    else -> riverArea[i]
                }
                else -> biomeArea[i]
            }
        }
        return result
    }
}

internal class VoronoiZoomLayer(seedMixup: Long, parent: OldGeneratorLayer) : OldGeneratorLayer(seedMixup) {
    init { this.parent = parent }

    override fun getArea(xo: Int, yo: Int, w: Int, h: Int): IntArray {
        val xo2 = xo - 2; val yo2 = yo - 2
        val bits = 2; val scale = 1 shl bits
        val px = xo2 shr bits; val py = yo2 shr bits
        val pw = (w shr bits) + 3; val ph = (h shr bits) + 3
        val parentArea = parent!!.getArea(px, py, pw, ph)

        val tmpWidth = pw shl bits
        val tmp = IntArray(tmpWidth * (ph shl bits))

        for (y in 0 until ph - 1) {
            var ul = parentArea[y * pw]
            var dl = parentArea[(y + 1) * pw]
            for (x in 0 until pw - 1) {
                val stretch = scale * 0.9
                initRandom((x + px).toLong() shl bits, (y + py).toLong() shl bits)
                val x0 = (nextRandom(1024) / 1024.0 - 0.5) * stretch
                val y0 = (nextRandom(1024) / 1024.0 - 0.5) * stretch
                initRandom((x + px + 1).toLong() shl bits, (y + py).toLong() shl bits)
                val x1 = (nextRandom(1024) / 1024.0 - 0.5) * stretch + scale
                val y1 = (nextRandom(1024) / 1024.0 - 0.5) * stretch
                initRandom((x + px).toLong() shl bits, (y + py + 1).toLong() shl bits)
                val x2 = (nextRandom(1024) / 1024.0 - 0.5) * stretch
                val y2 = (nextRandom(1024) / 1024.0 - 0.5) * stretch + scale
                initRandom((x + px + 1).toLong() shl bits, (y + py + 1).toLong() shl bits)
                val x3 = (nextRandom(1024) / 1024.0 - 0.5) * stretch + scale
                val y3 = (nextRandom(1024) / 1024.0 - 0.5) * stretch + scale

                val ur = parentArea[(x + 1) + y * pw]
                val dr = parentArea[(x + 1) + (y + 1) * pw]

                for (yy in 0 until scale) {
                    var index = ((y shl bits) + yy) * tmpWidth + (x shl bits)
                    for (xx in 0 until scale) {
                        val d0 = sq(yy - y0) + sq(xx - x0)
                        val d1 = sq(yy - y1) + sq(xx - x1)
                        val d2 = sq(yy - y2) + sq(xx - x2)
                        val d3 = sq(yy - y3) + sq(xx - x3)
                        tmp[index++] = when {
                            d0 < d1 && d0 < d2 && d0 < d3 -> ul
                            d1 < d0 && d1 < d2 && d1 < d3 -> ur
                            d2 < d0 && d2 < d1 && d2 < d3 -> dl
                            else -> dr
                        }
                    }
                }
                ul = ur; dl = dr
            }
        }

        val result = IntArray(w * h)
        for (y in 0 until h) {
            System.arraycopy(tmp, (y + (yo2 and (scale - 1))) * tmpWidth + (xo2 and (scale - 1)), result, y * w, w)
        }
        return result
    }

    private fun sq(value: Double) = value * value
}