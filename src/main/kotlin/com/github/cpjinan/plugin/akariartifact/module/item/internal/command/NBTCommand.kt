package com.github.cpjinan.plugin.akariartifact.module.item.internal.command

import com.github.cpjinan.plugin.akariartifact.core.utils.CommandUtil
import com.github.cpjinan.plugin.akariartifact.module.item.ModuleItem
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.nms.*
import taboolib.platform.util.isAir

object NBTCommand {
    val nbt = subCommand {
        if (!ModuleItem.isEnabledModule()) return@subCommand
        createHelper()

        literal("check") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                sender.castSafely<Player>().let {
                    val item = it?.inventory?.itemInMainHand
                    if (item.isAir()) {
                        sender.sendLang("Air-In-Hand")
                        return@execute
                    }

                    val itemTag = item.getItemTag()
                    sender.sendLang("NBT-Check")
                    fun sendItemTagInfo(player: Player, data: ItemTag, indent: String) {
                        for ((key, value) in data) {
                            when (value) {
                                is ItemTag -> {
                                    player.sendMessage("§7$indent$key:")
                                    sendItemTagInfo(player, value, "$indent  ")
                                }

                                is ItemTagList -> {
                                    player.sendMessage("&7$indent$key&8:".colored())
                                    value.forEach { v ->
                                        when (v.type) {
                                            ItemTagType.COMPOUND -> {
                                                sendItemTagInfo(player, v.asCompound(), "$indent  ")
                                            }

                                            else -> {
                                                player.sendMessage("$indent  &f- &f$v".colored())
                                            }
                                        }

                                    }
                                }

                                else -> {
                                    player.sendMessage("$indent&7$key&8: &f$value".colored())
                                }
                            }
                        }
                    }
                    sendItemTagInfo(sender.cast(), itemTag, "  ")
                }
            }
        }

        literal("set").dynamic("key").dynamic("value") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.castSafely<Player>()?.inventory?.itemInMainHand
                if (item.isAir()) {
                    sender.sendLang("Air-In-Hand")
                    return@execute
                }

                item.itemTagReader {
                    set(context["key"], context["value"])
                    write(item)
                }

                sender.sendLang("NBT-Set", context["key"], context["value"])
            }
        }.dynamic("options") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                val value = context["value"] + CommandUtil.parseContentAfterSpace(content.split(" "))
                val args = CommandUtil.parseOptions(content.split(" "))
                var silent = false

                for ((k, _) in args) {
                    when (k.lowercase()) {
                        "silent" -> silent = true
                    }
                }

                val item = sender.castSafely<Player>()?.inventory?.itemInMainHand
                if (item.isAir()) {
                    sender.sendLang("Air-In-Hand")
                    return@execute
                }

                item.itemTagReader {
                    set(context["key"], value)
                    write(item)
                }

                if (!silent) sender.sendLang("NBT-Set", context["key"], value)
            }
        }

        literal("remove").dynamic("key") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.castSafely<Player>()?.inventory?.itemInMainHand
                if (item.isAir()) {
                    sender.sendLang("Air-In-Hand")
                    return@execute
                }

                item.itemTagReader {
                    set(context["key"], null)
                    write(item)
                }

                sender.sendLang("NBT-Remove", context["key"])
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

                val item = sender.castSafely<Player>()?.inventory?.itemInMainHand
                if (item.isAir()) {
                    sender.sendLang("Air-In-Hand")
                    return@execute
                }

                item.itemTagReader {
                    set(context["key"], null)
                    write(item)
                }

                if (!silent) sender.sendLang("NBT-Remove", context["key"])
            }
        }
    }
}