package com.github.cpjinan.plugin.akariartifact.internal.manager

import com.github.cpjinan.plugin.akariartifact.AkariArtifact
import com.github.cpjinan.plugin.akariartifact.utils.LoggerUtil
import taboolib.module.chat.colored

object PluginManager {
    fun onEnable() {
        LoggerUtil.message(
            "",
            "&o      _    _              _    _         _   _  __            _    ".colored(),
            "&o     / \\  | | ____ _ _ __(_)  / \\   _ __| |_(_)/ _| __ _  ___| |_  ".colored(),
            "&o    / _ \\ | |/ / _` | '__| | / _ \\ | '__| __| | |_ / _` |/ __| __| ".colored(),
            "&o   / ___ \\|   < (_| | |  | |/ ___ \\| |  | |_| |  _| (_| | (__| |_  ".colored(),
            "&o  /_/   \\_\\_|\\_\\__,_|_|  |_/_/   \\_\\_|   \\__|_|_|  \\__,_|\\___|\\__| ".colored(),
            "",
            "&7正在加载 &3Akari&b&lLevel&7... &8${AkariArtifact.instance.description.version}".colored(),
            ""
        )
    }

    fun onDisable() {}
}