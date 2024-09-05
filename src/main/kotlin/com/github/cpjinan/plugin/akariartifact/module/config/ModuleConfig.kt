package com.github.cpjinan.plugin.akariartifact.module.config

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ModuleConfig {
    var settings: YamlConfiguration =
        YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, "core/settings.yml"))
    var arrow: YamlConfiguration =
        YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, "module/projectile.yml"))

    // Config Version
    const val VERSION = 1

    // Options
    fun getConfigVersion() = settings.getInt("Options.Config-Version")
    fun isEnabledCheckUpdate() = settings.getBoolean("Options.Check-Update")
    fun isEnabledSendMetrics() = settings.getBoolean("Options.Send-Metrics")
    fun isEnabledOPNotify() = settings.getBoolean("Options.OP-Notify")
    fun isEnabledDebug() = settings.getBoolean("Options.Debug")

    // Database
    fun getMethod() = settings.getString("Database.Method")
    fun getJsonSection() = settings.getConfigurationSection("Database.JSON")!!
    fun getCborSection() = settings.getConfigurationSection("Database.CBOR")!!
    fun getSqlTable() = settings.getString("Database.SQL.table")!!

    // Projectile
    fun isEnabledAutoRemoveArrow() = arrow.getBoolean("Arrow.Auto-Remove")
    fun getRemoveArrowDelay() = arrow.getLong("Arrow.Delay")

    // Save Default Resource
    fun saveDefaultResource() {
        plugin.saveDefaultResource(
            "core/settings.yml"
        )
        plugin.saveDefaultResource(
            "module/projectile.yml"
        )
        plugin.saveDefaultResource(
            "module/item/Example.yml",
        )
    }
}