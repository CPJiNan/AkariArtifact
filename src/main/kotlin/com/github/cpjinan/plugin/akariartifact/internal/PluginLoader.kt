package com.github.cpjinan.plugin.akariartifact.internal

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.internal.manager.ConfigManager
import com.github.cpjinan.plugin.akariartifact.internal.manager.LanguageManager
import com.github.cpjinan.plugin.akariartifact.utils.ItemUtil
import com.github.cpjinan.plugin.akariartifact.utils.LoggerUtil
import com.github.cpjinan.plugin.akariartifact.utils.UpdateUtil
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import taboolib.platform.util.buildItem

object PluginLoader {
    @Awake(LifeCycle.LOAD)
    fun load() {
        LanguageManager.saveDefaultResource()
        console().sendLang("Plugin-Loading", plugin.description.version)
        if (ConfigManager.isEnabledSendMetrics()) Metrics(18992, plugin.description.version, Platform.BUKKIT)
    }

    @Awake(LifeCycle.ENABLE)
    fun enable() {
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
        if (ConfigManager.isEnabledCheckUpdate()) UpdateUtil.getPluginUpdate()
        UpdateUtil.getPluginNotice()
        UpdateUtil.getConfigUpdate()

        ItemUtil.saveItem(
            buildItem(XMaterial.APPLE) {
                name = "&d坏黑的大苹果"
                lore.add("&7这是一个坏黑的大苹果")
                colored()
            },
            "item/Example.yml",
            "Apple"
        )
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        console().sendLang("Plugin-Disable")
    }

}