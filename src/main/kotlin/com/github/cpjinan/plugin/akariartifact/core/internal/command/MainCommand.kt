package com.github.cpjinan.plugin.akariartifact.core.internal.command

import com.github.cpjinan.plugin.akariartifact.core.common.PluginConfig
import com.github.cpjinan.plugin.akariartifact.module.gem.ModuleGem
import com.github.cpjinan.plugin.akariartifact.module.gem.internal.command.GemCommand
import com.github.cpjinan.plugin.akariartifact.module.item.ModuleItem
import com.github.cpjinan.plugin.akariartifact.module.item.internal.command.ItemCommand
import com.github.cpjinan.plugin.akariartifact.module.item.internal.command.LoreCommand
import com.github.cpjinan.plugin.akariartifact.module.item.internal.command.NBTCommand
import com.github.cpjinan.plugin.akariartifact.module.projectile.ModuleProjectile
import com.github.cpjinan.plugin.akariartifact.module.ui.ModuleUI
import com.github.cpjinan.plugin.akariartifact.module.ui.internal.command.UICommand
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang

@CommandHeader(name = "AkariArtifact", aliases = ["aa"])
object MainCommand {
    @CommandBody
    val main = mainCommand { createHelper() }

    @CommandBody(permission = "akariartifact.help", hidden = true)
    val help = subCommand { createHelper() }

    @CommandBody(permission = "akariartifact.item")
    val item = ItemCommand.item

    @CommandBody(permission = "akariartifact.lore")
    val lore = LoreCommand.lore

    @CommandBody(permission = "akariartifact.nbt")
    val nbt = NBTCommand.nbt

    @CommandBody(permission = "akariartifact.ui")
    val ui = UICommand.ui

    @CommandBody(permission = "akariartifact.gem")
    val gem = GemCommand.gem

    @CommandBody(permission = "akariartifact.reload")
    val reload = subCommand {
        execute { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
            PluginConfig.reloadConfig()
            ModuleItem.reloadConfig()
            ModuleUI.reloadConfig()
            ModuleProjectile.reloadConfig()
            ModuleGem.reloadConfig()
            sender.sendLang("Plugin-Reloaded")
        }
    }

}