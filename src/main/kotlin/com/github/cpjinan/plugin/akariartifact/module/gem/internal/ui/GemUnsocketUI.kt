package com.github.cpjinan.plugin.akariartifact.module.gem.internal.ui

import com.github.cpjinan.plugin.akariartifact.core.common.script.kether.Kether.evalKether
import com.github.cpjinan.plugin.akariartifact.module.gem.ModuleGem
import com.github.cpjinan.plugin.akariartifact.module.gem.api.GemAPI
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import com.github.cpjinan.plugin.akariartifact.module.ui.api.UIAPI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common5.util.replace
import taboolib.module.chat.colored
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.buildItem
import taboolib.platform.util.sendLang

object GemUnsocketUI {
    fun Player.openUnsocketUI(unsocketItem: ItemStack) {
        val uiConfig = UIAPI.getUIConfig()
        val ui = ModuleGem.getUnsocketUI()

        this.openMenu<PageableChest<Pair<String, ItemStack>>>(uiConfig.getString("$ui.Title")) {
            if (GemAPI.getItemSlotNames(unsocketItem).isEmpty()) {
                this@openUnsocketUI.sendLang("Gem-No-Slot")
                return
            }
            map(*(uiConfig.getStringList("$ui.Map").toTypedArray()))
            handLocked(true)

            onBuild { _, _ -> uiConfig.getStringList("$ui.Build").evalKether(this@openUnsocketUI) }
            onClose { uiConfig.getStringList("$ui.Close").evalKether(this@openUnsocketUI) }
            onClick { event -> event.isCancelled = true }

            val gemID = GemAPI.getItemSlotNames(unsocketItem).flatMap { slotName ->
                GemAPI.getGemDisplayNames().entries
                    .filter { it.value == slotName }
                    .map { it.key }
            }.distinct()

            elements {
                gemID.mapNotNull { id ->
                    val gemSection = GemAPI.getGemSections()[id] ?: return@mapNotNull null
                    val itemName = gemSection.getString("Item") ?: return@mapNotNull null
                    ItemAPI.getItem(itemName)?.let { itemStack -> id to itemStack }
                }
            }

            val slots = uiConfig.getConfigurationSection("$ui.Slot")
            slots.getKeys(false).forEach { slot ->
                when (uiConfig.getString("$ui.Slot.$slot.Default")) {
                    "Item" -> set(slot[0], unsocketItem)

                    "Element" -> {
                        slotsBy(slot[0])
                        onGenerate { player, element, _, _ ->
                            buildItem(element.second) {
                                val gemSection = GemAPI.getGemSections()[element.first] ?: return@buildItem
                                var infoLore = uiConfig.getStringList("$ui.Slot.$slot.Info").colored()

                                val slotPrefix = ModuleGem.getSlotPrefix()
                                val slotSuffix = ModuleGem.getSlotSuffix()

                                infoLore = infoLore.replacePlaceholder(this@openUnsocketUI).replace(
                                    "%Gem%" to "${slotPrefix}${gemSection.getString("Display")}${slotSuffix}",
                                    "%Slot%" to "${slotPrefix}${gemSection.getString("Slot")}${slotSuffix}",
                                    "%Chance%" to (gemSection.getDouble("Unsocket.Chance") * 100).toString(),
                                    "%Ready%" to GemAPI.isPlayerMetUnsocketCondition(
                                        player,
                                        unsocketItem,
                                        element.first
                                    )
                                )

                                uiConfig.getStringList("$ui.Slot.$slot.Replace").colored().forEach { replaceContent ->
                                    val (oldChar, newChar) = replaceContent.split("<=>", limit = 2)
                                    infoLore = infoLore.replace(oldChar to newChar)
                                }

                                lore.addAll(infoLore)
                            }
                        }
                        onClick { _, element ->
                            this@openUnsocketUI.performCommand("AkariArtifact gem unsocket ${element.first}")
                            this@openUnsocketUI.performCommand("AkariArtifact ui close ${this@openUnsocketUI.name} --silent")
                        }
                    }

                    "Previous-Page" -> {
                        setPreviousPage(getFirstSlot(slot[0])) { _, hasPreviousPage ->
                            val itemSection = uiConfig.getConfigurationSection("$ui.Slot.$slot.Item")
                            if (hasPreviousPage) {
                                ItemAPI.getItem(itemSection.getString("Available")) ?: ItemStack(Material.AIR)
                            } else {
                                ItemAPI.getItem(itemSection.getString("Unavailable")) ?: ItemStack(Material.AIR)
                            }
                        }
                    }

                    "Next-Page" -> {
                        setNextPage(getFirstSlot(slot[0])) { _, hasNextPage ->
                            val itemSection = uiConfig.getConfigurationSection("$ui.Slot.$slot.Item")
                            if (hasNextPage) {
                                ItemAPI.getItem(itemSection.getString("Available")) ?: ItemStack(Material.AIR)
                            } else {
                                ItemAPI.getItem(itemSection.getString("Unavailable")) ?: ItemStack(Material.AIR)
                            }
                        }
                    }

                    else -> {
                        val itemName = uiConfig.getString("$ui.Slot.$slot.Item")
                        ItemAPI.getItem(itemName)?.let { item ->
                            set(slot[0], item) {
                                when {
                                    clickEvent().isLeftClick && !clickEvent().isShiftClick -> uiConfig.getStringList("$ui.Slot.$slot.Left-Click")
                                        ?.evalKether(this@openUnsocketUI)

                                    clickEvent().isRightClick && !clickEvent().isShiftClick -> uiConfig.getStringList("$ui.Slot.$slot.Right-Click")
                                        ?.evalKether(this@openUnsocketUI)

                                    clickEvent().isLeftClick && clickEvent().isShiftClick -> uiConfig.getStringList("$ui.Slot.$slot.Shift-Left-Click")
                                        ?.evalKether(this@openUnsocketUI)

                                    clickEvent().isRightClick && clickEvent().isShiftClick -> uiConfig.getStringList("$ui.Slot.$slot.Shift-Right-Click")
                                        ?.evalKether(this@openUnsocketUI)
                                }
                            }
                        } ?: this@openUnsocketUI.sendLang("Item-Not-Found")
                    }
                }
            }
        }
    }
}