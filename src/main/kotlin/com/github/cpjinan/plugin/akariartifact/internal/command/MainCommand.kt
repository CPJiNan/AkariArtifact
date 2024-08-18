package com.github.cpjinan.plugin.akarilevel.internal.command

import com.github.cpjinan.plugin.akariartifact.internal.command.subcommand.LoreCommand
import com.github.cpjinan.plugin.akariartifact.internal.manager.ConfigManager
import com.github.cpjinan.plugin.akariartifact.utils.FileUtil
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang
import java.io.File

@CommandHeader(name = "AkariArtifact")
object MainCommand {
    @CommandBody
    val main = mainCommand { createHelper() }

    @CommandBody(permission = "akariartifact.help", hidden = true)
    val help = mainCommand { createHelper() }

    @CommandBody(permission = "akarilevel.lore")
    val level = LoreCommand.lore

    @CommandBody(permission = "akariartifact.reload")
    val reload = subCommand {
        execute { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
            ConfigManager.settings = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, "settings.yml"))
            sender.sendLang("Plugin-Reloaded")
        }
    }

}