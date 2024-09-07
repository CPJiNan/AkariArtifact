package com.github.cpjinan.plugin.akariartifact.core.common

import com.github.cpjinan.plugin.akariartifact.core.utils.LoggerUtil.message
import com.github.cpjinan.plugin.akariartifact.core.utils.VersionUtil.toSemanticVersion
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common5.util.replace
import taboolib.module.chat.colored
import taboolib.module.lang.asLangTextList
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.asLangTextList
import java.net.HttpURLConnection
import java.net.URL

object PluginUpdate {
    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        if (PluginConfig.isEnabledCheckUpdate()) getPluginUpdate()
        getPluginNotice()
    }

    @SubscribeEvent
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.isOp && PluginConfig.isEnabledOPNotify()) sendPlayerUpdateNotify(event.player)
    }

    private fun getPluginNotice() {
        Thread {
            val urlConnection =
                URL("https://cpjinan.github.io/Pages/AkariArtifact/notice.html").openConnection() as HttpURLConnection
            try {
                val message = urlConnection.inputStream.bufferedReader().readText()
                if (message.isNotBlank()) message(message.colored())
            } catch (_: java.net.ConnectException) {
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }

    private fun getPluginUpdate() {
        Thread {
            val urlConnection =
                URL("https://cpjinan.github.io/Pages/AkariArtifact/version.html").openConnection() as HttpURLConnection
            try {
                val latestVersion = urlConnection.inputStream.bufferedReader().readText().toSemanticVersion()!!
                val currentVersion = BukkitPlugin.getInstance().description.version.toSemanticVersion()!!
                if (latestVersion > currentVersion) {
                    console().asLangTextList("Plugin-Update")
                        .replace(Pair("%latestVersion%", latestVersion), Pair("%currentVersion%", currentVersion))
                        .forEach {
                            message(it.colored())
                        }
                }
            } catch (_: java.net.ConnectException) {
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }

    private fun sendPlayerUpdateNotify(player: Player) {
        Thread {
            val urlConnection =
                URL("https://cpjinan.github.io/Pages/AkariArtifact/version.html").openConnection() as HttpURLConnection
            try {
                val latestVersion = urlConnection.inputStream.bufferedReader().readText().toSemanticVersion()!!
                val currentVersion = BukkitPlugin.getInstance().description.version.toSemanticVersion()!!
                if (latestVersion > currentVersion) {
                    player.asLangTextList("Plugin-Update")
                        .replace(Pair("%latestVersion%", latestVersion), Pair("%currentVersion%", currentVersion))
                        .forEach {
                            player.sendMessage(it.colored())
                        }
                }
            } catch (_: java.net.ConnectException) {
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}