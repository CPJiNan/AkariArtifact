package com.github.cpjinan.plugin.akariartifact.module.gem.internal.ui

import com.github.cpjinan.plugin.akariartifact.core.common.script.kether.Kether.evalKether
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Chest
import taboolib.platform.util.sendLang

object GemSocketUI {
    fun Player.openSocketUI(config: YamlConfiguration, ui: String) {
        player.openMenu<Chest>(config.getString("$ui.Title")) {
            map(*(config.getStringList("$ui.Map").toTypedArray()))
            handLocked(true)

            onBuild { _, _ ->
                config.getStringList("$ui.Build").evalKether(player)
            }

            onClose {
                config.getStringList("$ui.Close").evalKether(player)
            }

            val slots = config.getConfigurationSection("$ui.Slot")
            slots.getKeys(false).forEach { slot ->
                val item = ItemAPI.getItem(config.getString("$ui.Slot.$slot.Item"))
                if (item == null) {
                    player.sendLang("Item-Not-Found")
                    return
                }

                set(slot[0], item) {
                    config.getStringList("$ui.Slot.$slot.Click").evalKether(player)
                }
            }

        }
    }
}