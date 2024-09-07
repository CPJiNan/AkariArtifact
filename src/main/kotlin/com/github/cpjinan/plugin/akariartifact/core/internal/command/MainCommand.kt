package com.github.cpjinan.plugin.akariartifact.core.internal.command

import com.github.cpjinan.plugin.akariartifact.core.common.PluginConfig
import com.github.cpjinan.plugin.akariartifact.module.projectile.ModuleProjectile
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang

@CommandHeader(name = "AkariArtifact")
object MainCommand {
    @CommandBody
    val main = mainCommand { createHelper() }

    @CommandBody(permission = "akariartifact.help", hidden = true)
    val help = mainCommand { createHelper() }

    @CommandBody(permission = "akariartifact.reload")
    val reload = subCommand {
        execute { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
            PluginConfig.settings = YamlConfiguration.loadConfiguration(PluginConfig.settingsFile)
            ModuleProjectile.config = YamlConfiguration.loadConfiguration(ModuleProjectile.configFile)
            sender.sendLang("Plugin-Reloaded")
        }
    }

}