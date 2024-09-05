package com.github.cpjinan.plugin.akariartifact.module.update.listener

import com.github.cpjinan.plugin.akariartifact.module.config.ModuleConfig
import com.github.cpjinan.plugin.akariartifact.module.update.utils.UpdateUtil
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

object UpdateListener {
    @SubscribeEvent
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.isOp && ModuleConfig.isEnabledOPNotify()) UpdateUtil.sendPlayerUpdateNotify(event.player)
    }
}