package com.github.cpjinan.plugin.akariartifact.core.common

import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object PluginConfig {
    var settingsFile = File(FileUtil.dataFolder, "core/settings.yml")
    var settings: YamlConfiguration =
        YamlConfiguration.loadConfiguration(settingsFile)

    // Config Version
    const val VERSION = 1

    // Options
    fun getConfigVersion() = settings.getInt("Options.Config-Version")
    fun isEnabledCheckUpdate() = settings.getBoolean("Options.Check-Update")
    fun isEnabledSendMetrics() = settings.getBoolean("Options.Send-Metrics")
    fun isEnabledOPNotify() = settings.getBoolean("Options.OP-Notify")
    fun isEnabledDebug() = settings.getBoolean("Options.Debug")
}