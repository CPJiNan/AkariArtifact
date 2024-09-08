package com.github.cpjinan.plugin.akariartifact.core.internal

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.core.utils.LoggerUtil
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.simpleCommand
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.util.countItem
import taboolib.platform.util.giveItem

object PluginLoader {
    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        plugin.saveDefaultResource(
            "core/settings.yml"
        )
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
            ItemAPI.saveItem(sender.cast<Player>().inventory.itemInMainHand, "module/item/Example.yml", args[0])
        }
        simpleCommand("getItem") { sender, args ->
            sender.cast<Player>().giveItem(ItemAPI.getItem("module/item/Example.yml", args[0]))
        }
        simpleCommand("checkAmount") { sender, _ ->
            val matcher: (ItemStack) -> Boolean = { itemStack ->
                itemStack == sender.cast<Player>().inventory
            }
            sender.sendMessage(sender.cast<Player>().inventory.countItem(matcher).toString())
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun onDisable() {
        console().sendLang("Plugin-Disable")
    }

}