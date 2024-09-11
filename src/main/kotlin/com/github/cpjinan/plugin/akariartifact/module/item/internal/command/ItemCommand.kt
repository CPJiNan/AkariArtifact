package com.github.cpjinan.plugin.akariartifact.module.item.internal.command

import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.getConfigSections
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import com.github.cpjinan.plugin.akariartifact.module.item.ModuleItem
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.int
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.nms.getName
import taboolib.platform.util.giveItem
import java.io.File

object ItemCommand {
    private val itemFiles = FileUtil.getFile(File(FileUtil.dataFolder, "module/item"), true)
        .filter { it.name.endsWith(".yml") }.toCollection(ArrayList())
    private val itemSections = itemFiles.getConfigSections()
    private val itemNames = itemSections.map { it.key }.toCollection(ArrayList())
    private val itemConfig = ConfigUtil.getMergedConfig(itemSections)

    val item = subCommand {
        if (!ModuleItem.isEnabledModule()) return@subCommand
        createHelper()

        literal("list") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                sender.sendLang("Item-List")
                sender.sendMessage(itemNames.joinToString(separator = "&7, ".colored()) { "&f${it}".colored() })
            }
        }

        literal("get").dynamic("id").int("amount") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val item = ItemAPI.getItem(itemConfig, context["id"])
                if (item != null) {
                    sender.cast<Player>().giveItem(item, context.int("amount"))
                    sender.sendLang("Item-Get", item.getName(sender.cast()), context.int("amount"))
                } else sender.sendLang("Item-Not-Found")
            }
        }
    }
}