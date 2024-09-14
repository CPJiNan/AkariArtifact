package com.github.cpjinan.plugin.akariartifact.module.gem.internal.ui

import com.github.cpjinan.plugin.akariartifact.core.common.script.kether.Kether.evalKether
import com.github.cpjinan.plugin.akariartifact.module.gem.ModuleGem
import com.github.cpjinan.plugin.akariartifact.module.gem.api.GemAPI
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import com.github.cpjinan.plugin.akariartifact.module.ui.api.UIAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common5.util.replace
import taboolib.module.chat.colored
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.buildItem
import taboolib.platform.util.sendLang

object GemSocketUI {
    fun Player.openSocketUI(socketItem: ItemStack) {
        val uiConfig = UIAPI.getUIConfig()
        val ui = ModuleGem.getUI()

        player.openMenu<PageableChest<Pair<String, ItemStack>>>(uiConfig.getString("$ui.Title")) {
            map(*(uiConfig.getStringList("$ui.Map").toTypedArray()))
            handLocked(true)

            onBuild { _, _ -> uiConfig.getStringList("$ui.Build").evalKether(player) }
            onClose { uiConfig.getStringList("$ui.Close").evalKether(player) }

            // 获取宝石列表
            val gemID = GemAPI.getItemSlotNames(socketItem).flatMap { slotName ->
                GemAPI.getGemSlotNames().entries
                    .filter { it.value == slotName }
                    .map { it.key }
            }.distinct()

            // 获取宝石数据
            elements {
                gemID.mapNotNull { id ->
                    val gemSection = GemAPI.getGemSections()[id] ?: return@mapNotNull null
                    val itemName = gemSection.getString("Item") ?: return@mapNotNull null
                    ItemAPI.getItem(itemName)?.let { itemStack -> id to itemStack }
                }
            }

            // 设置物品槽位
            val slots = uiConfig.getConfigurationSection("$ui.Slot")
            slots.getKeys(false).forEach { slot ->
                when (uiConfig.getString("$ui.Slot.$slot.Default")) {
                    "Item" -> set(slot[0], socketItem)

                    "Element" -> {
                        onGenerate { player, element, _, _ ->
                            buildItem(element.second) {
                                val gemSection = GemAPI.getGemSections()[element.first] ?: return@buildItem
                                val infoLore = uiConfig.getStringList("$ui.Slot.$slot.Lore").colored()

                                infoLore.replacePlaceholder(this@openSocketUI)
                                infoLore.replace(
                                    "%Gem%" to gemSection.getString("Display"),
                                    "%Slot%" to gemSection.getString("Slot"),
                                    "%Chance%" to (gemSection.getDouble("Socket.Chance") * 100).toString(),
                                    "%Ready%" to GemAPI.isPlayerMetSocketCondition(player, socketItem, element.first)
                                )

                                uiConfig.getStringList("$ui.Slot.$slot.Replace").colored().forEach { replaceContent ->
                                    val (oldChar, newChar) = replaceContent.split("<=>", limit = 2)
                                    infoLore.replace(oldChar to newChar)
                                }
                            }
                        }
                    }

                    else -> {
                        val itemName = uiConfig.getString("$ui.Slot.$slot.Item")
                        ItemAPI.getItem(itemName)?.let { item ->
                            set(slot[0], item) {
                                uiConfig.getStringList("$ui.Slot.$slot.Click").evalKether(player)
                            }
                        } ?: player.sendLang("Item-Not-Found")
                    }
                }
            }
        }
    }

}