package com.github.cpjinan.plugin.akariartifact.module.projectile.internal.listener

import com.github.cpjinan.plugin.akariartifact.module.projectile.ModuleProjectile
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

object ProjectileListener {
    @SubscribeEvent
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        if (ModuleProjectile.isEnabledAutoRemoveArrow() && event.entity is Arrow && event.entity.shooter is Player) {
            submit(async = true, delay = ModuleProjectile.getRemoveArrowDelay()) {
                event.entity.takeIf { !it.isOnGround && !it.isDead }?.remove()
            }
        }
    }

    @SubscribeEvent
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (ModuleProjectile.isEnabledAutoRemoveArrow() && event.entity is Arrow) event.entity.remove()
    }
}