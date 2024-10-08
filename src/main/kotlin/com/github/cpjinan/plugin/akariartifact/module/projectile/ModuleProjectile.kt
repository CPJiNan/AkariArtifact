package com.github.cpjinan.plugin.akariartifact.module.projectile

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.File

object ModuleProjectile {
    const val MODULE_NAME = "Projectile"
    const val MODULE_VERSION = 1

    var configFile: File = File(FileUtil.dataFolder, "module/projectile.yml")
    var config: YamlConfiguration = YamlConfiguration()

    init {
        reloadConfig()
    }

    fun reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile)
    }

    fun isEnabledModule() = config.getBoolean("Enable")
    fun isEnabledAutoRemoveArrow() = config.getBoolean("Arrow.Auto-Remove")
    fun getRemoveArrowDelay() = config.getLong("Arrow.Delay")

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        plugin.saveDefaultResource(
            "module/projectile.yml"
        )
        reloadConfig()
    }
}