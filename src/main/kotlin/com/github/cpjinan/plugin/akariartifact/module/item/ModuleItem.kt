package com.github.cpjinan.plugin.akariartifact.module.item

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.File

object ModuleItem {
    const val MODULE_NAME = "Item"
    const val MODULE_VERSION = 2

    var configFile = File(FileUtil.dataFolder, "module/item.yml")
    var config: YamlConfiguration = YamlConfiguration()

    init {
        reloadConfig()
    }

    fun reloadConfig() {
        YamlConfiguration.loadConfiguration(configFile)
        ItemAPI.reloadItem()
    }

    fun isEnabledModule() = config.getBoolean("Enable")

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        plugin.saveDefaultResource(
            "module/item.yml"
        )
        plugin.saveDefaultResource(
            "module/item/Example.yml"
        )
        plugin.saveDefaultResource(
            "module/item/GemItems.yml"
        )
        reloadConfig()
    }
}