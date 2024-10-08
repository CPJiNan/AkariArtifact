package com.github.cpjinan.plugin.akariartifact.core.internal

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.LoggerUtil
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang

object PluginLoader {
    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        console().sendLang("Plugin-Loading", plugin.description.version)
    }

    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        LoggerUtil.message(
            "",
            "&o     _    _              _    _         _   _  __            _".colored(),
            "&o    / \\  | | ____ _ _ __(_)  / \\   _ __| |_(_)/ _| __ _  ___| |_".colored(),
            "&o   / _ \\ | |/ / _` | '__| | / _ \\ | '__| __| | |_ / _` |/ __| __|".colored(),
            "&o  / ___ \\|   < (_| | |  | |/ ___ \\| |  | |_| |  _| (_| | (__| |_".colored(),
            "&o /_/   \\_\\_|\\_\\__,_|_|  |_/_/   \\_\\_|   \\__|_|_|  \\__,_|\\___|\\__|".colored(),
            ""
        )
        console().sendLang("Plugin-Enabled")
    }

    @Awake(LifeCycle.DISABLE)
    fun onDisable() {
        console().sendLang("Plugin-Disable")
    }

}