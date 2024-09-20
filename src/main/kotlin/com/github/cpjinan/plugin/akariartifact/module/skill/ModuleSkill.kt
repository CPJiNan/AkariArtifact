package com.github.cpjinan.plugin.akariartifact.module.skill

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.File

object ModuleSkill {
    const val MODULE_NAME = "Skill"
    const val MODULE_VERSION = 1

    var configFile = File(FileUtil.dataFolder, "module/skill.yml")
    var config: YamlConfiguration = YamlConfiguration()

    init {
        reloadConfig()
    }

    fun reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile)
    }

    fun isEnabledModule() = config.getBoolean("Enable")

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        plugin.saveDefaultResource(
            "module/skill.yml"
        )
        plugin.saveDefaultResource(
            "module/skill/Example.yml"
        )
        reloadConfig()
    }
}