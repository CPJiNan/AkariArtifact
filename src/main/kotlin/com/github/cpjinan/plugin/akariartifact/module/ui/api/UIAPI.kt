package com.github.cpjinan.plugin.akariartifact.module.ui.api

import com.github.cpjinan.plugin.akariartifact.core.common.script.kether.Kether.evalKether
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.getConfigSections
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Chest
import taboolib.platform.util.sendLang
import java.io.File

object UIAPI {
    private var uiFiles: ArrayList<File> = arrayListOf()
    private var uiSections: HashMap<String, ConfigurationSection> = hashMapOf()
    private var uiNames: ArrayList<String> = arrayListOf()
    private var uiConfig: YamlConfiguration = YamlConfiguration()

    init {
        reloadUI()
    }

    /**
     * 为玩家打开指定 UI
     * @param ui UI 名称
     * @author CPJiNan
     */
    fun Player.openUI(ui: String) {
        openUIForPlayer(this, uiConfig, ui)
    }

    /**
     * 为玩家打开指定 UI
     * @param config 配置文件
     * @param ui UI 名称
     * @author CPJiNan
     */
    fun Player.openUI(config: YamlConfiguration, ui: String) {
        openUIForPlayer(this, config, ui)
    }

    /**
     * 为玩家打开指定 UI
     * @param file 配置文件
     * @param ui UI 名称
     * @author CPJiNan
     */
    fun Player.openUI(file: File, ui: String) {
        val config = YamlConfiguration.loadConfiguration(file)
        openUIForPlayer(this, config, ui)
    }

    /**
     * 为玩家打开指定 UI
     * @param file 配置文件路径 (插件目录为根目录)
     * @param ui UI 名称
     * @author CPJiNan
     */
    fun Player.openUI(file: String, ui: String) {
        val config = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, file))
        openUIForPlayer(this, config, ui)
    }

    /**
     * 获取所有 UI 的配置文件
     * @return UI 配置文件列表
     * @author CPJiNan
     */
    fun getUIFiles(): ArrayList<File> = uiFiles

    /**
     * 获取所有 UI 的配置节点
     * @return UI 配置节点列表 (由 UI 名称 及其 配置节点 组成)
     * @author CPJiNan
     */
    fun getUISections(): HashMap<String, ConfigurationSection> = uiSections

    /**
     * 获取所有 UI 的名称
     * @return UI 名称列表
     * @author CPJiNan
     */
    fun getUINames(): ArrayList<String> = uiNames

    /**
     * 获取所有 UI 配置合并后的新配置
     * @return UI 配置
     * @author CPJiNan
     */
    fun getUIConfig(): YamlConfiguration = uiConfig

    /**
     * 重载 UI 配置文件
     * @author CPJiNan
     */
    fun reloadUI() {
        uiFiles = FileUtil.getFile(File(FileUtil.dataFolder, "module/ui"), true)
            .filter { it.name.endsWith(".yml") }.toCollection(ArrayList())
        uiSections = uiFiles.getConfigSections()
        uiNames = uiSections.map { it.key }.toCollection(ArrayList())
        uiConfig = ConfigUtil.getMergedConfig(uiSections)
    }

    private fun openUIForPlayer(player: Player, config: YamlConfiguration, ui: String) {
        player.openMenu<Chest>(config.getString("$ui.Title")) {
            map(*(config.getStringList("$ui.Map").toTypedArray()))
            handLocked(true)

            onBuild { _, _ ->
                config.getStringList("$ui.Build").evalKether(player)
            }

            onClose {
                config.getStringList("$ui.Close").evalKether(player)
            }

            val slots = config.getConfigurationSection("$ui.Slot")
            slots.getKeys(false).forEach { slot ->
                val item = ItemAPI.getItem(config.getString("$ui.Slot.$slot.Item"))
                if (item == null) {
                    player.sendLang("Item-Not-Found")
                    return
                }

                set(slot[0], item) {
                    config.getStringList("$ui.Slot.$slot.Click").evalKether(player)
                }
            }

        }
    }
}