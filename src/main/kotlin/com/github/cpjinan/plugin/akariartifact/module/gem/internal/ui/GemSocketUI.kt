package com.github.cpjinan.plugin.akariartifact.module.gem.internal.ui

import com.github.cpjinan.plugin.akariartifact.core.common.script.kether.Kether.evalKether
import com.github.cpjinan.plugin.akariartifact.module.gem.ModuleGem
import com.github.cpjinan.plugin.akariartifact.module.gem.api.GemAPI
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
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
        val config = GemAPI.getGemConfig()
        val ui = ModuleGem.getUI()
        player.openMenu<PageableChest<Pair<String, ItemStack>>>(config.getString("$ui.Title")) {
            map(*(config.getStringList("$ui.Map").toTypedArray()))
            handLocked(true)

            onBuild { _, _ ->
                config.getStringList("$ui.Build").evalKether(player)
            }

            onClose {
                config.getStringList("$ui.Close").evalKether(player)
            }

            val gemID = mutableListOf<String>()
            GemAPI.getItemSlotNames(socketItem).forEach { slotName ->
                val matchingKeys = mutableListOf<String>()
                for ((key, mapValue) in GemAPI.getGemSlotNames()) {
                    if (mapValue == slotName) {
                        matchingKeys.add(key)
                    }
                }
                gemID.addAll(matchingKeys)
            }
            gemID.distinct()

            elements {
                gemID.mapNotNull { id ->
                    val gemSection = GemAPI.getGemSections()[id] ?: return@mapNotNull null
                    val itemName = gemSection.getString("Item") ?: return@mapNotNull null
                    val itemStack = ItemAPI.getItem(itemName) ?: return@mapNotNull null

                    id to itemStack
                }
            }

            val slots = config.getConfigurationSection("$ui.Slot")
            slots.getKeys(false).forEach { slot ->
                when (config.getString("$ui.Slot.$slot.Default")) {
                    "Item" -> {
                        set(slot[0], socketItem)
                    }

                    "Element" -> {
                        onGenerate { player, element, _, _ ->
                            buildItem(element.second) {
                                val infoLore = config.getStringList("$ui.Slot.$slot.Lore").colored()

                                infoLore.replacePlaceholder(this@openSocketUI)

                                infoLore.replace(
                                    Pair("%Gem%", GemAPI.getGemSections()[element.first]!!.getString("Display")),
                                    Pair("%Slot%", GemAPI.getGemSections()[element.first]!!.getString("Slot")),
                                    Pair(
                                        "%Chance%",
                                        GemAPI.getGemSections()[element.first]!!.getDouble("Socket.Chance") * 100
                                    ),
                                    Pair(
                                        "%Ready%",
                                        GemAPI.isPlayerMetSocketCondition(player, socketItem, element.first)
                                    )
                                )

                                config.getStringList("$ui.Slot.$slot.Replace").colored().forEach { replaceContent ->
                                    val (oldChar, newChar) = replaceContent.split("<=>", limit = 2)
                                    infoLore.replace(Pair(oldChar, newChar))
                                }
                            }
                        }
                    }

                    else -> {
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
    }
}