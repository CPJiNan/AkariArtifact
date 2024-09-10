package com.github.cpjinan.plugin.akariartifact.module.item.internal.command

import com.github.cpjinan.plugin.akariartifact.module.item.ModuleItem
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.module.configuration.util.asMap
import taboolib.module.lang.sendLang
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.ItemTagType
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir

object NBTCommand {
    val nbt = subCommand {
        if (!ModuleItem.isEnabledModule()) return@subCommand
        createHelper()

        literal("check") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                try {
                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        if (item.isAir()) {
                            sender.sendLang("Air-In-Hand")
                            return@execute
                        }

                        val itemTag = item.getItemTag()
                        sender.sendLang("NBT-Check")
                        fun runAny(player: Player, data: ItemTag, indent: String) {
                            for ((key, value) in data) {
                                when (value) {
                                    is ItemTag -> {
                                        player.sendMessage("§7$indent$key:")
                                        runAny(player, value, "$indent  ")
                                    }

                                    is ItemTagList -> {
                                        player.sendMessage("&7$indent$key&8:".colored())
                                        value.forEach { v ->
                                            when (v.type) {
                                                ItemTagType.COMPOUND -> {
                                                    runAny(player, v.asCompound(), "$indent  ")
                                                }

                                                else -> {
                                                    player.sendMessage("$indent  &f- &f$value".colored())
                                                }
                                            }

                                        }
                                    }

                                    is List<*> -> {
                                        player.sendMessage("§7$indent$key:")
                                        value.forEach { u ->
                                            when (u) {
                                                is ItemTagList -> {
                                                    player.sendMessage("&7$indent$key&8:".colored())
                                                    u.forEach { v ->
                                                        player.sendMessage("$indent  &f- &f$v".colored())
                                                    }
                                                }

                                                is Map<*, *> -> {
                                                    player.sendMessage("&7$indent$key&8:".colored())
                                                    value.forEach { o ->
                                                        o.asMap().forEach { (k, v) ->
                                                            player.sendMessage("$indent  &f$k: $v".colored())
                                                        }
                                                    }
                                                }

                                                else -> player.sendMessage("$indent  &f- &f$value".colored())
                                            }
                                        }
                                    }

                                    is Map<*, *> -> {
                                        player.sendMessage("§7$indent$key:")
                                        value.forEach { u ->
                                            u.asMap().forEach { (k, v) ->
                                                player.sendMessage("$indent  &f$k: $v".colored())
                                            }
                                        }
                                    }

                                    else -> {
                                        player.sendMessage("$indent&7$key&8: &f$value".colored())
                                    }
                                }
                            }
                        }
                        runAny(sender.cast(), itemTag, "  ")
                    }
                } catch (error: IndexOutOfBoundsException) {
                    sender.sendLang("Index-Out-Of-Bounds")
                }
            }
        }
    }
}