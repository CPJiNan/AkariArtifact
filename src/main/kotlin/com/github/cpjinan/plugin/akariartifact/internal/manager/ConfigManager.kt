package com.github.cpjinan.plugin.akariartifact.internal.manager

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.utils.FileUtil
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ConfigManager {
    var settings: YamlConfiguration = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, "settings.yml"))

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

    // Save Default Resource
    fun saveDefaultResource() {
        plugin.saveDefaultResource(
            "settings.yml",
            File(FileUtil.dataFolder, "settings.yml")
        )
        plugin.saveDefaultResource(
            "item/Example.yml",
            File(FileUtil.dataFolder, "item/Example.yml")
        )
    }
}