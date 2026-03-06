package org.kanelucky.config.world

import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.annotation.Comment

/**
 * @author Kanelucky
 */
enum class WorldType {
    NORMAL,
    SUPERFLAT
}

class SuperflatConfig : OkaeriConfig() {
    @Comment("Available presets: classic, bottomless_pit, snowy_kingdom, void")
    var preset: String = "classic"
}

class WorldSettings : OkaeriConfig() {
    @Comment("World type: NORMAL or SUPERFLAT")
    var worldType: WorldType = WorldType.NORMAL

    @Comment("World generation seed")
    var seed: Long = 1234567890

    @Comment("Superflat configuration")
    var superflat: SuperflatConfig = SuperflatConfig()
}
