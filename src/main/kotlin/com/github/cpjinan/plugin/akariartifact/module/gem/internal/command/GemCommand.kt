package com.github.cpjinan.plugin.akariartifact.module.gem.internal.command

import com.github.cpjinan.plugin.akariartifact.module.gem.api.GemAPI
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand

object GemCommand {
    val gem = subCommand {
        literal("socket").dynamic("gem") {
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