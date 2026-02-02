//package org.kanelucky.server.permission
//
//import dev.rollczi.litecommands.meta.MetaHolder
//import dev.rollczi.litecommands.permission.PermissionResolver
//import dev.rollczi.litecommands.permission.PermissionValidationResult
//import dev.rollczi.litecommands.platform.PlatformSender
//import dev.rollczi.litecommands.permission.PermissionValidationResult.Verdict.
//import net.minestom.server.entity.Player
//
//class PermissionResolvers : PermissionResolver {
//    override fun resolve(
//        sender: PlatformSender,
//        metaHolder: MetaHolder
//    ): MutableList<PermissionValidationResult.Verdict> {
//        if (sender !is Player) return mutableListOf()
//        if (sender.permissionLevel < 4) return mutableListOf(PermissionValidationResult.Verdict.permitted())
//
//        return mutableListOf()
//    }
//}