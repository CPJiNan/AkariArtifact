package com.github.cpjinan.plugin.akariartifact

import com.github.cpjinan.plugin.akariartifact.internal.manager.PluginManager
import taboolib.common.platform.Plugin
import taboolib.platform.BukkitPlugin

object AkariArtifact : Plugin() {

    val instance by lazy { BukkitPlugin.getInstance() }

    override fun onEnable() {
        PluginManager.onEnable()
    }

    override fun onDisable() {
        PluginManager.onDisable()
    }
}