package com.github.cpjinan.plugin.akariartifact.common.listener

import com.github.cpjinan.plugin.akariartifact.internal.manager.ConfigManager
import com.github.cpjinan.plugin.akariartifact.utils.UpdateUtil
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

object PlayerListener {
    @SubscribeEvent
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.isOp && ConfigManager.isEnabledOPNotify()) UpdateUtil.sendPlayerUpdateNotify(event.player)
    }
}