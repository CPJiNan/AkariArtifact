package com.github.cpjinan.plugin.akariartifact.module.update

import com.github.cpjinan.plugin.akariartifact.module.config.ModuleConfig
import com.github.cpjinan.plugin.akariartifact.module.update.utils.UpdateUtil
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object ModuleUpdate {
    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        if (ModuleConfig.isEnabledCheckUpdate()) UpdateUtil.getPluginUpdate()
        UpdateUtil.getPluginNotice()
        UpdateUtil.getConfigUpdate()
    }
}