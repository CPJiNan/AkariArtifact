package com.github.cpjinan.plugin.akariartifact.module.gem

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.File

object ModuleGem {
    const val MODULE_NAME = "Gem"
    const val MODULE_VERSION = 1

    var configFile = File(FileUtil.dataFolder, "module/gem.yml")
    var config: YamlConfiguration =
        YamlConfiguration.loadConfiguration(configFile)

    fun isEnabledModule() = config.getBoolean("Enable")
    fun getUI() = config.getString("UI.Socket")
    fun getSlotPrefix() = config.getString("Slot.Prefix")
    fun getSlotSuffix() = config.getString("Slot.Suffix")

    @Awake(LifeCycle.CONST)
    fun onLoad() {
        plugin.saveDefaultResource(
            "module/gem.yml"
        )
        plugin.saveDefaultResource(
            "module/gem/Example.yml"
        )
    }
}