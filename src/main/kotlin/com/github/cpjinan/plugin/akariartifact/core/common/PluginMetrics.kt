package com.github.cpjinan.plugin.akariartifact.core.common

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.module.config.ModuleConfig
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.module.metrics.Metrics

object PluginMetrics {
    @Awake(LifeCycle.LOAD)
    fun load() {
        if (ModuleConfig.isEnabledSendMetrics()) Metrics(18992, plugin.description.version, Platform.BUKKIT)
    }
}