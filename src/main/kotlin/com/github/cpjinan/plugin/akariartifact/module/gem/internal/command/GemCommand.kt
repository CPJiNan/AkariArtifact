package com.github.cpjinan.plugin.akariartifact.module.gem.internal.command

import com.github.cpjinan.plugin.akariartifact.module.gem.api.GemAPI
import com.github.cpjinan.plugin.akariartifact.module.gem.internal.ui.GemSocketUI.openSocketUI
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest

object GemCommand {
    val gem = subCommand {
        literal("open") {
            literal("socket") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
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
    }
}