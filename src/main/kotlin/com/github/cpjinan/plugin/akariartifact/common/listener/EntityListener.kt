package com.github.cpjinan.plugin.akariartifact.common.listener

import com.github.cpjinan.plugin.akariartifact.internal.manager.ConfigManager
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

object EntityListener {
    @SubscribeEvent
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        if (ConfigManager.isEnabledAutoRemoveArrow() && event.entity is Arrow && event.entity.shooter is Player) {
            submit(async = true, delay = 20) {
                event.entity.takeIf { !it.isOnGround && !it.isDead }?.remove()
            }
        }
    }
    @SubscribeEvent
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (ConfigManager.isEnabledAutoRemoveArrow() && event.entity is Arrow) event.entity.remove()
    }
}