package com.github.cpjinan.plugin.akariartifact

import com.github.cpjinan.plugin.akariartifact.utils.FileUtil
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin

object AkariArtifact : Plugin() {

    val instance by lazy { BukkitPlugin.getInstance() }

    override fun onEnable() {}

    override fun onDisable() {}
}