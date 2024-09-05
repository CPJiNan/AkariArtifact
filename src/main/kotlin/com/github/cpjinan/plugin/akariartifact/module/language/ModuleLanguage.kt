package com.github.cpjinan.plugin.akariartifact.module.language

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.lang.Language

object ModuleLanguage {
    @Awake(LifeCycle.CONST)
    fun onConst() {
        // 设置语言文件路径
        Language.path = "core/lang"
    }
}