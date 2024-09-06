package com.github.cpjinan.plugin.akariartifact.core.common

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.lang.Language

object PluginLanguage {
    @Awake(LifeCycle.CONST)
    fun onConst() {
        Language.path = "core/lang"
    }
}