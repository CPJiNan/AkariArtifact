package com.github.cpjinan.plugin.akariartifact.module.gem.internal.command

import com.github.cpjinan.plugin.akariartifact.core.utils.CommandUtil
import com.github.cpjinan.plugin.akariartifact.module.gem.api.GemAPI
import com.github.cpjinan.plugin.akariartifact.module.gem.internal.ui.GemSocketUI.openSocketUI
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.module.lang.sendLang
import taboolib.platform.util.modifyLore

object GemCommand {
    val gem = subCommand {
        literal("open") {
            literal("socket") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                    val item = sender.cast<Player>().inventory.itemInMainHand
                    sender.cast<Player>().openSocketUI(item)
                }
            }
        }

        literal("socket").dynamic("gem") {
            suggest { GemAPI.getGemNames() }
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().inventory.itemInMainHand
                GemAPI.socketGem(sender.cast(), item, context["gem"])
            }
        }.dynamic("options", optional = true) {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().inventory.itemInMainHand
                GemAPI.socketGem(sender.cast(), item, context["gem"])
            }
        }

        literal("slot") {
            literal("add").dynamic("gem", optional = false) {
                suggest { GemAPI.getGemNames() }
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    val item = sender.cast<Player>().inventory.itemInMainHand
                    val slot = GemAPI.getGemSections()[context["gem"]]?.getString("Slot") ?: return@execute

                    item.modifyLore {
                        add(slot)
                    }

                    sender.sendLang("Gem-Slot-Add", slot)
                }
            }.dynamic("options", optional = true) {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    val args = CommandUtil.parseOptions(content.split(" "))
                    var silent = false

                    for ((k, _) in args) {
                        when (k.lowercase()) {
                            "silent" -> silent = true
                        }
                    }

                    val item = sender.cast<Player>().inventory.itemInMainHand
                    val slot = GemAPI.getGemSections()[context["gem"]]?.getString("Slot") ?: return@execute

                    item.modifyLore {
                        add(slot)
                    }

                    if (!silent) sender.sendLang("Gem-Slot-Add", slot)
                }
            }
        }
    }
}