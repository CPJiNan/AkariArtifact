package com.github.cpjinan.plugin.akariartifact.module.item

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object ModuleItem {
    const val MODULE_NAME = "Item"
    const val MODULE_VERSION = 1

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        plugin.saveDefaultResource(
            "module/item/Example.yml",
        )
    }
}