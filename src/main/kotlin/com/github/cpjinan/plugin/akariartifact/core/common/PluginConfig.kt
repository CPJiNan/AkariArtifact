package com.github.cpjinan.plugin.akariartifact.core.common

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

object PluginConfig {
    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        plugin.saveDefaultResource(
            "core/settings.yml"
        )
    }

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