package com.github.cpjinan.plugin.akariartifact.core.internal.command

import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import com.github.cpjinan.plugin.akariartifact.module.config.ModuleConfig
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

    @CommandBody(permission = "akariartifact.reload")
    val reload = subCommand {
        execute { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
            ModuleConfig.settings = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, "core/settings.yml"))
            ModuleConfig.arrow = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, "module/projectile.yml"))
            sender.sendLang("Plugin-Reloaded")
        }
    }

}