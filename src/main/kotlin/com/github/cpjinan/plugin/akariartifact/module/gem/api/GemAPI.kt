package com.github.cpjinan.plugin.akariartifact.module.gem.api

import com.github.cpjinan.plugin.akariartifact.core.common.script.kether.Kether.evalKether
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.getConfigSections
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import com.github.cpjinan.plugin.akariartifact.module.gem.ModuleGem
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import org.black_ixx.playerpoints.PlayerPoints
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.compat.depositBalance
import taboolib.platform.compat.getBalance
import taboolib.platform.util.giveItem
import taboolib.platform.util.hasItem
import taboolib.platform.util.modifyLore
import taboolib.platform.util.takeItem
import java.io.File
import kotlin.random.Random

object GemAPI {
    private var gemFiles: ArrayList<File> = arrayListOf()
    private var gemSections: HashMap<String, ConfigurationSection> = hashMapOf()
    private var gemNames: ArrayList<String> = arrayListOf()
    private var gemConfig: YamlConfiguration = YamlConfiguration()

    init {
        reloadGem()
    }

    /**
     * 重载宝石配置文件
     * @author CPJiNan
     */
    fun reloadGem() {
        gemFiles = FileUtil.getFile(File(FileUtil.dataFolder, "module/gem"), true)
            .filter { it.name.endsWith(".yml") }.toCollection(ArrayList())
        gemSections = gemFiles.getConfigSections()
        gemNames = gemSections.map { it.key }.toCollection(ArrayList())
        gemConfig = ConfigUtil.getMergedConfig(gemSections)
    }

    /**
     * 为指定物品镶嵌宝石
     * @author CPJiNan
     */
    fun socketGem(player: Player, item: ItemStack, gem: String): Boolean {
        val gemSection = gemSections[gem] ?: return false

        val gemItem = gemSection.getString("Item") ?: return false
        val gemSlot = gemSection.getString("Slot") ?: return false
        val gemDisplay = gemSection.getString("Display") ?: return false
        val gemAttribute = gemSection.getStringList("Attribute")

        val socketChance = gemSection.getDouble("Socket.Chance", 0.0)
        val socketCondition = gemSection.getStringList("Socket.Condition")
        val socketMoneyCost = gemSection.getDouble("Socket.Cost.Money", 0.0)
        val socketPointCost = gemSection.getInt("Socket.Cost.Point", 0)
        val socketItemCost = gemSection.getStringList("Socket.Cost.Item")
        val socketIsReturnItem = gemSection.getBoolean("Socket.Return-Item", true)

        if (!player.inventory.hasItem { it.amount == 1 }) {
            player.sendMessage("手中物品数量需为 1")
            return false
        }

        val gemItemStack = ItemAPI.getItem(gemItem)
        if (!player.inventory.hasItem { it == gemItemStack }) {
            player.sendMessage("宝石不满足")
            return false
        }
        player.inventory.takeItem { it == gemItemStack }

        if (Random.nextDouble(1.0) < socketChance) {
            player.sendMessage("几率不满足")
            if (socketIsReturnItem) player.giveItem(gemItemStack)
            return false
        }

        if (!socketCondition.all { it.evalKether(player).toString().toBoolean() }) {
            player.sendMessage("条件不满足")
            return false
        }

        if (Bukkit.getServer().pluginManager.isPluginEnabled("Vault") && player.getBalance() < socketMoneyCost) {
            player.sendMessage("金钱不满足")
            return false
        } else player.depositBalance(socketMoneyCost)

        if (Bukkit.getServer().pluginManager.isPluginEnabled("PlayerPoints") && socketPointCost > 0 && PlayerPoints.getInstance().api.look(player.uniqueId) < socketPointCost) {
            player.sendMessage("点券不满足")
            return false
        } else if (socketPointCost > 0) {
            PlayerPoints.getInstance().api.take(player.uniqueId, socketPointCost)
        }

        val hasEnoughItems = socketItemCost.all { itemCost ->
            val (itemName, amount) = itemCost.split("<=>", limit = 2)
            player.inventory.hasItem(amount.toInt()) { it == ItemAPI.getItem(itemName) }
        }

        if (!hasEnoughItems) {
            player.sendMessage("物品不满足")
            return false
        }

        val meta = item.itemMeta ?: return false
        val lore = meta.lore ?: mutableListOf()

        var matchLine: Int? = null

        val slotPrefix = ModuleGem.getSlotPrefix()
        val slotSuffix = ModuleGem.getSlotSuffix()
        val searchString = "$slotPrefix$gemSlot$slotSuffix"
        val replaceString = "$slotPrefix$gemDisplay$slotSuffix"

        for (i in lore.indices) {
            if (lore[i].contains(searchString)) {
                lore[i] = lore[i].replace(searchString, replaceString)
                matchLine = i
                break
            }
        }

        if (matchLine == null) {
            player.sendMessage("物品 Lore 不满足")
            return false
        }

        gemAttribute.forEachIndexed { index, attribute ->
            item.modifyLore { add(matchLine + index + 1, attribute) }
        }

        meta.lore = lore
        item.itemMeta = meta

        return true
    }
}