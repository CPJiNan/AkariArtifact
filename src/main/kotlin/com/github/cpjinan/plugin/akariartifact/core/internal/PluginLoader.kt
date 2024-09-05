package com.github.cpjinan.plugin.akariartifact.core.internal

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.LoggerUtil
import com.github.cpjinan.plugin.akariartifact.module.config.ModuleConfig
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import com.github.cpjinan.plugin.akariartifact.module.update.utils.UpdateUtil
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.command.simpleCommand
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics

object PluginLoader {
    @Awake(LifeCycle.LOAD)
    fun load() {
        ModuleConfig.saveDefaultResource()
        console().sendLang("Plugin-Loading", plugin.description.version)
        if (ModuleConfig.isEnabledSendMetrics()) Metrics(18992, plugin.description.version, Platform.BUKKIT)
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
        simpleCommand("testSave") { sender, args ->
            val item = sender.cast<Player>().inventory.itemInMainHand
            ItemAPI.saveItem(item, "item/Example.yml", "TestItem")
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        console().sendLang("Plugin-Disable")
    }

}