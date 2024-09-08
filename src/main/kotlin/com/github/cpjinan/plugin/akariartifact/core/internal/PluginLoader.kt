package com.github.cpjinan.plugin.akariartifact.core.internal

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.LoggerUtil
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.simpleCommand
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.util.giveItem

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
        simpleCommand("saveItem") { sender, args ->
            if (args.isNotEmpty()) {
                ItemAPI.saveItem(
                    sender.cast<Player>().inventory.itemInMainHand,
                    "module/item/Example.yml",
                    args[0]
                )
                sender.sendMessage("&7已将手中物品存入 &fmodule/item/Example.yml &7中的 &f${args[0]} &7.".colored())
            } else sender.sendMessage("&c用法: /saveItem 物品编辑名".colored())
        }
        simpleCommand("getItem") { sender, args ->
            if (args.isNotEmpty()) {
                val item = ItemAPI.getItem("module/item/Example.yml", args[0])
                if (item != null) {
                    sender.cast<Player>().giveItem(item)
                    sender.sendMessage("&7已将从 &fmodule/item/Example.yml &7中的 &f${args[0]} &7构建物品到手中.".colored())
                } else sender.sendMessage("&c未从 module/item/Example.yml 中的 ${args[0]} 找到指定物品.".colored())
            } else sender.sendMessage("&c用法: /getItem 物品编辑名".colored())
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun onDisable() {
        console().sendLang("Plugin-Disable")
    }

}