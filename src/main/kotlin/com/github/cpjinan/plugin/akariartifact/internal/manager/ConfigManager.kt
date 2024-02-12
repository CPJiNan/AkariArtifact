package com.github.cpjinan.plugin.akariartifact.internal.manager

import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object ConfigManager {
    @Config("settings.yml", autoReload = false)
    lateinit var settings: ConfigFile

    // Options
    fun getConfigVersion() = settings.getInt("Options.Config-Version")
    fun isEnabledCheckUpdate() = settings.getBoolean("Options.Check-Update")
    fun isEnabledSendMetrics() = settings.getBoolean("Options.Send-Metrics")
    fun isEnabledDebug() = settings.getBoolean("Options.Debug")
}