package com.github.cpjinan.plugin.akariartifact.module.gem.internal.command

import com.github.cpjinan.plugin.akariartifact.core.utils.CommandUtil
import com.github.cpjinan.plugin.akariartifact.module.gem.ModuleGem
import com.github.cpjinan.plugin.akariartifact.module.gem.api.GemAPI
import com.github.cpjinan.plugin.akariartifact.module.gem.internal.ui.GemSocketUI.openSocketUI
import com.github.cpjinan.plugin.akariartifact.module.gem.internal.ui.GemUnsocketUI.openUnsocketUI
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir
import taboolib.platform.util.modifyLore

@Suppress("DEPRECATION")
object GemCommand {
    val gem = subCommand {
        literal("open") {
            literal("socket") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                    val item = sender.cast<Player>().itemInHand
                    sender.cast<Player>().openSocketUI(item)
                }
            }
            literal("unsocket") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                    val item = sender.cast<Player>().itemInHand
                    sender.cast<Player>().openUnsocketUI(item)
                }
            }
        }

        literal("socket").dynamic("gem") {
            suggest { GemAPI.getGemNames() }
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().itemInHand
                if (item.isAir()) {
                    sender.sendLang("Air-In-Hand")
                    return@execute
                }

                GemAPI.socketGem(sender.cast(), item, context["gem"])
            }
        }.dynamic("options", optional = true) {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().itemInHand
                if (item.isAir()) {
                    sender.sendLang("Air-In-Hand")
                    return@execute
                }

                GemAPI.socketGem(sender.cast(), item, context["gem"])
            }
        }

        literal("unsocket").dynamic("gem") {
            suggest { GemAPI.getGemNames() }
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().itemInHand
                if (item.isAir()) {
                    sender.sendLang("Air-In-Hand")
                    return@execute
                }

                GemAPI.unsocketGem(sender.cast(), item, context["gem"])
            }
        }.dynamic("options", optional = true) {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().itemInHand
                if (item.isAir()) {
                    sender.sendLang("Air-In-Hand")
                    return@execute
                }

                GemAPI.unsocketGem(sender.cast(), item, context["gem"])
            }
        }

        literal("slot") {
            literal("check") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                    val item = sender.cast<Player>().itemInHand
                    if (item.isNotAir()) {
                        sender.sendLang("Gem-Slot-Check")
                        GemAPI.getItemSlotNames(item).forEachIndexed { index, content ->
                            sender.sendMessage("&7${index + 1} &8| &r${ModuleGem.getSlotPrefix()}$content${ModuleGem.getSlotSuffix()}".colored())
                        }
                    } else sender.sendLang("Air-In-Hand")
                }
            }

            literal("add").dynamic("gem", optional = false) {
                suggest { GemAPI.getGemNames() }
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    val item = sender.cast<Player>().itemInHand
                    if (item.isAir()) {
                        sender.sendLang("Air-In-Hand")
                        return@execute
                    }

                    val slot = GemAPI.getGemSections()[context["gem"]]?.getString("Slot") ?: return@execute
                    val slotPrefix = ModuleGem.getSlotPrefix()
                    val slotSuffix = ModuleGem.getSlotSuffix()

                    item.modifyLore {
                        add("$slotPrefix$slot$slotSuffix")
                    }

                    sender.sendLang("Gem-Slot-Add", "$slotPrefix$slot$slotSuffix")
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

                    val item = sender.cast<Player>().itemInHand
                    if (item.isAir()) {
                        if (!silent) sender.sendLang("Air-In-Hand")
                        return@execute
                    }

                    val slot = GemAPI.getGemSections()[context["gem"]]?.getString("Slot") ?: return@execute
                    val slotPrefix = ModuleGem.getSlotPrefix()
                    val slotSuffix = ModuleGem.getSlotSuffix()

                    item.modifyLore {
                        add("$slotPrefix$slot$slotSuffix")
                    }

                    if (!silent) sender.sendLang("Gem-Slot-Add", "$slotPrefix$slot$slotSuffix")
                }
            }
        }
    }
}