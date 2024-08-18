package com.github.cpjinan.plugin.akariartifact.internal.command.subcommand

import com.github.cpjinan.plugin.akariartifact.utils.CommandUtil
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.int
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

                    for ((k, _) in args) {
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

        literal("remove") {
            int("line") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        item?.modifyLore {
                            removeAt(context.int("line") - 1)
                            colored()
                        }
                        sender.sendLang("Remove-Lore", context.int("line"))
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    val args = CommandUtil.parseOptions(content.split(" "))
                    var silent = false

                    for ((k, _) in args) {
                        when (k.lowercase()) {
                            "silent" -> silent = true
                        }
                    }

                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        item?.modifyLore {
                            removeAt(context.int("line") - 1)
                            colored()
                        }
                    }

                    if (!silent) sender.sendLang("Remove-Lore", context.int("line"))
                }
            }
        }

        literal("set") {
            int("line").dynamic("lore") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        item?.modifyLore {
                            set(context.int("line") - 1, context["lore"])
                            colored()
                        }
                        sender.sendLang("Set-Lore", context.int("line"), context["lore"])
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    val args = CommandUtil.parseOptions(content.split(" "))
                    var silent = false

                    for ((k, _) in args) {
                        when (k.lowercase()) {
                            "silent" -> silent = true
                        }
                    }

                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        item?.modifyLore {
                            set(context.int("line") - 1, context["lore"])
                            colored()
                        }
                    }

                    if (!silent) sender.sendLang("Set-Lore", context.int("line"), context["lore"])
                }
            }
        }

        literal("insert") {
            int("line").dynamic("lore") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        item?.modifyLore {
                            add(context.int("line") - 1, context["lore"])
                            colored()
                        }
                        sender.sendLang("Insert-Lore", context.int("line"), context.int("line") + 1, context["lore"])
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    val args = CommandUtil.parseOptions(content.split(" "))
                    var silent = false

                    for ((k, _) in args) {
                        when (k.lowercase()) {
                            "silent" -> silent = true
                        }
                    }

                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        item?.modifyLore {
                            add(context.int("line") - 1, context["lore"])
                            colored()
                        }
                    }

                    if (!silent) sender.sendLang(
                        "Insert-Lore",
                        context.int("line"),
                        context.int("line") + 1,
                        context["lore"]
                    )
                }
            }
        }
    }
}