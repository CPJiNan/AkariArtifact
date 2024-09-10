package com.github.cpjinan.plugin.akariartifact.module.item.internal.command

import com.github.cpjinan.plugin.akariartifact.module.item.ModuleItem
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir

object NBTCommand {
    val nbt = subCommand {
        if (!ModuleItem.isEnabledModule()) return@subCommand
        createHelper()
        literal("info") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                try {
                    sender.castSafely<Player>().let { it ->
                        val item = it?.inventory?.itemInMainHand
                        if (item.isAir()) {
                            sender.sendLang("Air-In-Hand")
                            return@execute
                        }

                        val itemTag = item.getItemTag()
                        itemTag.entries.forEach() {
                            // 这个不用限制的，可以全部显示
                            // 另外这个 Lore 可能会有多级
                            //直接这样看看效果咋样的
                            sender.sendMessage("&7$it".colored())
                        }
                    }
                } catch (error: IndexOutOfBoundsException) {
                    sender.sendLang("Index-Out-Of-Bounds")
                }
            }
        }
    }
}