package org.kanelucky.server.permission

import dev.rollczi.litecommands.meta.MetaHolder
import dev.rollczi.litecommands.permission.PermissionResolver
import dev.rollczi.litecommands.permission.PermissionValidationResult
import dev.rollczi.litecommands.platform.PlatformSender

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

class ServerPermissionResolver : PermissionResolver {

    private val PUBLIC_PERMISSIONS = setOf(
        "minestom4fun.commands.defaults.version",
        "minestom4fun.commands.defaults.tps"
    )

    private val delegate = PermissionResolver.createDefault(CommandSender::class.java) { sender, permission ->
        if (permission in PUBLIC_PERMISSIONS) return@createDefault true
        if (sender is Player) sender.permissionLevel >= 4
        else true
    }

    override fun resolve(sender: PlatformSender, metaHolder: MetaHolder): List<PermissionValidationResult.Verdict> {
        return delegate.resolve(sender, metaHolder)
    }
}