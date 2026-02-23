package org.kanelucky.server.player.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minestom.server.coordinate.Pos

import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

import java.io.File
import java.util.UUID

/**
 * @author Kanelucky
 */
@Serializable
data class PlayerData(
    val uuid: String,
    val username: String,
    val gameMode: String = "SURVIVAL",
    val health: Float = 20f,
    val food: Int = 20,
    val saturation: Float = 5f,
    val xpLevel: Int = 0,
    val xpProgress: Float = 0f,
    val x: Double = 0.0,
    val y: Double = 64.0,
    val z: Double = 0.0,
    val yaw: Float = 0f,
    val pitch: Float = 0f,
    val permissionLevel: Int = 0
)

object PlayerDataManager {

    private val folder = File("players")
    private val json = Json { prettyPrint = true }

    init {
        if (!folder.exists()) folder.mkdirs()
    }

    private fun fileOf(uuid: UUID) = File(folder, "$uuid.json")

    fun save(player: Player) {
        val data = PlayerData(
            uuid = player.uuid.toString(),
            username = player.username,
            gameMode = player.gameMode.name,
            health = player.health,
            food = player.food,
            saturation = player.foodSaturation,
            xpLevel = player.level,
            xpProgress = player.exp,
            x = player.position.x,
            y = player.position.y,
            z = player.position.z,
            yaw = player.position.yaw,
            pitch = player.position.pitch,
            permissionLevel = player.permissionLevel
        )
        fileOf(player.uuid).writeText(json.encodeToString(PlayerData.serializer(), data))
    }

    fun load(player: Player) {
        val file = fileOf(player.uuid)
        if (!file.exists()) return

        val data = json.decodeFromString(PlayerData.serializer(), file.readText())
        player.gameMode = GameMode.valueOf(data.gameMode)
        player.health = data.health
        player.food = data.food
        player.foodSaturation = data.saturation
        player.level = data.xpLevel
        player.exp = data.xpProgress
        player.permissionLevel = data.permissionLevel
        player.teleport(Pos(data.x, data.y, data.z, data.yaw, data.pitch))
    }
}