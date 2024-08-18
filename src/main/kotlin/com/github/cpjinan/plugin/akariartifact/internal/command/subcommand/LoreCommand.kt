package com.github.cpjinan.plugin.akariartifact.internal.command.subcommand

import com.github.cpjinan.plugin.akariartifact.utils.CommandUtil
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.util.modifyLore

object LoreCommand {
    val lore = subCommand {
        literal("add") {
            dynamic("lore") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        item?.modifyLore {
                            add(context["lore"])
                            colored()
                        }
                        sender.sendLang("Add-Lore", context["lore"])
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    val args = CommandUtil.parseOptions(content.split(" "))
                    var silent = false

                    for ((k, v) in args) {
                        when (k.lowercase()) {
                            "silent" -> silent = true
                        }
                    }

                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        item?.modifyLore {
                            add(context["lore"])
                            colored()
                        }
                    }

                    if (!silent) sender.sendLang("Add-Lore", context["lore"])
                }
            }
        }
    }
}