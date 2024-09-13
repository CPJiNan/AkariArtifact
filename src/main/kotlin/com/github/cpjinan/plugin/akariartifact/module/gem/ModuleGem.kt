package com.github.cpjinan.plugin.akariartifact.module.gem

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import com.github.cpjinan.plugin.akariartifact.module.item.ModuleItem
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

    fun isEnabledModule() = ModuleItem.config.getBoolean("Enable")
    fun getUIName() = ModuleItem.config.getString("UI")

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        plugin.saveDefaultResource(
            "module/gem.yml"
        )
    }
}