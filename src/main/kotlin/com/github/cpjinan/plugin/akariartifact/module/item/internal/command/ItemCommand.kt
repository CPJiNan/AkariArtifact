package com.github.cpjinan.plugin.akariartifact.module.item.internal.command

import com.github.cpjinan.plugin.akariartifact.core.utils.CommandUtil
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI.reloadItem
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.nms.getName
import taboolib.platform.util.giveItem

object ItemCommand {
    val item = subCommand {
        createHelper()

        literal("get").dynamic("id") {
            suggest { ItemAPI.getItemNames() }
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = ItemAPI.getItem(ItemAPI.getItemConfig(), context["id"])
                if (item != null) {
                    sender.cast<Player>().giveItem(item)
                    sender.sendLang("Item-Get", context["id"], 1)
                } else sender.sendLang("Item-Not-Found")
            }
        }.int("amount", optional = true) {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = ItemAPI.getItem(ItemAPI.getItemConfig(), context["id"])
                if (item != null) {
                    sender.cast<Player>().giveItem(item, context.int("amount"))
                    sender.sendLang("Item-Get", context["id"], context.int("amount"))
                } else sender.sendLang("Item-Not-Found")
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

                val item = ItemAPI.getItem(ItemAPI.getItemConfig(), context["id"])
                if (item != null) {
                    sender.cast<Player>().giveItem(item, context.int("amount"))
                    if (!silent) sender.sendLang("Item-Get", context["id"], context.int("amount"))
                } else if (!silent) sender.sendLang("Item-Not-Found")
            }
        }

        literal("give").player("player").dynamic("id") {
            suggest { ItemAPI.getItemNames() }
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = ItemAPI.getItem(ItemAPI.getItemConfig(), context["id"])
                if (item != null) {
                    val player = context.player("player").cast<Player>()
                    player.giveItem(item)
                    sender.sendLang("Item-Give", context["id"], 1, context["player"])
                } else sender.sendLang("Item-Not-Found")
            }
        }.int("amount", optional = true) {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = ItemAPI.getItem(ItemAPI.getItemConfig(), context["id"])
                if (item != null) {
                    val player = context.player("player").cast<Player>()
                    player.giveItem(item, context.int("amount"))
                    sender.sendLang("Item-Give", context["id"], context.int("amount"), context["player"])
                } else sender.sendLang("Item-Not-Found")
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

                val item = ItemAPI.getItem(ItemAPI.getItemConfig(), context["id"])
                if (item != null) {
                    val player = context.player("player").cast<Player>()
                    player.giveItem(item, context.int("amount"))
                    if (!silent) sender.sendLang("Item-Give", context["id"], context.int("amount"), context["player"])
                } else if (!silent) sender.sendLang("Item-Not-Found")
            }
        }

        literal("save") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().inventory.itemInMainHand
                ItemAPI.saveItem(
                    item,
                    "module/item/SaveItems.yml",
                    item.getName(sender.cast())
                )
                reloadItem()
                sender.sendLang("Item-Save", "SaveItems.yml", item.getName(sender.cast()))
            }
        }.dynamic("id") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().inventory.itemInMainHand
                ItemAPI.saveItem(
                    item,
                    "module/item/SaveItems.yml",
                    context["id"]
                )
                reloadItem()
                sender.sendLang("Item-Save", "SaveItems.yml", context["id"])
            }
        }.dynamic("path") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = sender.cast<Player>().inventory.itemInMainHand
                ItemAPI.saveItem(
                    item,
                    "module/item/${context["path"].takeIf { it.endsWith(".yml") } ?: "${context["path"]}.yml"}",
                    context["id"]
                )
                reloadItem()
                sender.sendLang(
                    "Item-Save",
                    context["path"].takeIf { it.endsWith(".yml") } ?: "${context["path"]}.yml",
                    context["id"]
                )
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

                val item = sender.cast<Player>().inventory.itemInMainHand
                ItemAPI.saveItem(
                    item,
                    "module/item/${context["path"].takeIf { it.endsWith(".yml") } ?: "${context["path"]}.yml"}",
                    context["id"]
                )
                reloadItem()
                if (!silent) sender.sendLang(
                    "Item-Save",
                    context["path"].takeIf { it.endsWith(".yml") } ?: "${context["path"]}.yml",
                    context["id"]
                )
            }
        }

        literal("list") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                sender.sendLang("Item-List")
                sender.sendMessage(
                    ItemAPI.getItemNames().joinToString(separator = "&7, ".colored()) { "&f${it}".colored() })
            }
        }
    }
}