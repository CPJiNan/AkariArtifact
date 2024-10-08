package com.github.cpjinan.plugin.akariartifact.module.gem

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import com.github.cpjinan.plugin.akariartifact.module.gem.api.GemAPI
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.File

object ModuleGem {
    const val MODULE_NAME = "Gem"
    const val MODULE_VERSION = 1

    var configFile = File(FileUtil.dataFolder, "module/gem.yml")
    var config: YamlConfiguration = YamlConfiguration()

    init {
        reloadConfig()
    }

    fun reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile)
        GemAPI.reloadGem()
    }

    fun isEnabledModule() = config.getBoolean("Enable")
    fun getSocketUI() = config.getString("UI.Socket") ?: "GemSocketUI"
    fun getUnsocketUI() = config.getString("UI.Unsocket") ?: "GemUnsocketUI"
    fun getSlotPrefix() = config.getString("Slot.Prefix") ?: "§7「"
    fun getSlotSuffix() = config.getString("Slot.Suffix") ?: "§7」"

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        plugin.saveDefaultResource(
            "module/gem.yml"
        )
        plugin.saveDefaultResource(
            "module/gem/Example.yml"
        )
        reloadConfig()
    }
}