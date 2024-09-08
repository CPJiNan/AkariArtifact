package com.github.cpjinan.plugin.akariartifact.module.item.api

import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import github.saukiya.sxattribute.SXAttribute
import ink.ptms.um.Mythic
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import taboolib.module.chat.colored
import taboolib.module.nms.*
import java.io.File

object ItemAPI {
    /**
     * 保存物品至配置文件
     * @param item 物品
     * @param file 配置文件
     * @param path 配置项路径
     * @author CPJiNan
     */
    fun saveItem(item: ItemStack, file: File, path: String) {
        val config = YamlConfiguration.loadConfiguration(file)
        config.set(path, null)
        saveItemToConfig(item, config, path)
        config.save(file)
    }

    /**
     * 保存物品至配置文件
     * @param item 物品
     * @param file 配置文件路径 (插件目录为根目录)
     * @param path 配置项路径
     * @author CPJiNan
     */
    fun saveItem(item: ItemStack, file: String, path: String) {
        val config = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, file))
        config.set(path, null)
        saveItemToConfig(item, config, path)
        config.save(File(FileUtil.dataFolder, file))
    }

    /**
     * 从配置文件获取物品
     * @param file 配置文件
     * @param path 配置项路径
     * @author CPJiNan
     */
    fun getItem(file: File, path: String): ItemStack {
        val config = YamlConfiguration.loadConfiguration(file)
        return getItemFromConfig(config, path)
    }

    /**
     * 从配置文件获取物品
     * @param file 配置文件路径 (插件目录为根目录)
     * @param path 配置项路径
     * @author CPJiNan
     */
    fun getItem(file: String, path: String): ItemStack {
        val config = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, file))
        return getItemFromConfig(config, path)
    }

    /**
     * 从其他插件获取物品
     * @param plugin 插件名称
     * @param key 物品索引
     * @param player 执行玩家 (默认为 null)
     * @return 插件下指定索引的 ItemStack
     * @author CPJiNan
     */
    fun getItem(plugin: String, key: String, player: Player? = null): ItemStack? {
        val item: ItemStack? = when (plugin) {
            "MythicMobs" -> Mythic.API.getItemStack(key)

            "SX-Attribute" -> SXAttribute.getApi().getItem(key, player)

            else -> throw IllegalArgumentException("Unable to find item $key in plugin $plugin.")
        }
        return item
    }

    private fun saveItemToConfig(item: ItemStack, config: YamlConfiguration, path: String) {
        val meta = item.itemMeta

        // 基本属性
        config.set("$path.Type", item.type.name)
        item.durability.takeIf { it != 0.toShort() }?.let { config.set("$path.Data", it.toInt()) }
        config.set("$path.Display", item.getName())
        meta.lore?.let { config.set("$path.Lore", it) }

        // 附魔属性
        if (meta.hasEnchants()) {
            config.set("$path.Options.Glow", true)
            meta.enchants.forEach { (enchantment, level) ->
                config.set("$path.Enchantments.${enchantment.name}", level)
            }
        }

        // 常规物品设置
        meta.isUnbreakable.takeIf { it }?.let { config.set("$path.Options.Unbreakable", it) }
        meta.itemFlags.takeIf { it.isNotEmpty() }?.map { it.name }?.let { config.set("$path.Options.HideFlags", it) }

        // 特殊物品设置
        when (meta) {
            // 旗帜
            is BannerMeta -> {
                val patterns = meta.patterns.map { "${it.color.name}-${it.pattern.identifier}" }
                config.set("$path.Options.BannerPatterns", patterns)
            }
            // 皮革护甲
            is LeatherArmorMeta -> config.set("$path.Options.Color", meta.color.asRGB())
            // 药水
            is PotionMeta -> {
                val potionTypeName = meta.basePotionData.type.name
                val isExtended = meta.basePotionData.isExtended
                val isUpgraded = meta.basePotionData.isUpgraded
                val effects = meta.customEffects.map { "${it.type.name}-${it.amplifier}-${it.duration}" }
                config.set("$path.Options.BasePotionData.Type", potionTypeName)
                config.set("$path.Options.BasePotionData.Extended", isExtended)
                config.set("$path.Options.BasePotionData.Upgraded", isUpgraded)
                config.set("$path.Options.PotionEffects", effects)
            }
            // 头颅
            is SkullMeta -> {
                meta.owner?.let { config.set("$path.Options.SkullOwner", it) }
            }
        }

        // NBT
        val itemTag = item.getItemTag()
        itemTag.entries.forEach {
            if (it.key !in listOf(
                    "display",
                    "ench",
                    "Unbreakable",
                    "HideFlags",
                    "BlockEntityTag",
                    "Potion",
                    "SkullOwner"
                )
            ) {
                /**
                 * 获取多节点 NBT 数据
                 * @author 晓劫
                 */
                fun runAny(config: YamlConfiguration, key: String, value: ItemTagData) {
                    when (val data = value.unsafeData()) {
                        // 基本类型
                        is Byte -> config.set(key, data)
                        is Short -> config.set(key, data)
                        is Int -> config.set(key, data)
                        is Long -> config.set(key, data)
                        is Float -> config.set(key, data)
                        is Double -> config.set(key, data)
                        is String -> config.set(key, data)
                        is Boolean -> config.set(key, data)
                        // 数组和列表
                        is ItemTag -> data.entries.forEach { runAny(config, "$key.${it.key}", it.value) }
                        is ItemTagList -> {
                            val list : MutableList<Any> = mutableListOf()
                            data.forEach { list.add(it.unsafeData()) }
                            config.set(key, list)
                        }
                        is ByteArray -> config.set(key, data)
                        is IntArray -> config.set(key, data)
                        is LongArray -> config.set(key, data)
                        // 未知类型
                        else -> throw IllegalArgumentException("Unknown data type.")
                    }
                }

                runAny(config, "$path.NBT.${it.key}", it.value)
            }
        }
    }
}

private fun getItemFromConfig(config: YamlConfiguration, path: String): ItemStack {
    val type = Material.getMaterial(config.getString("$path.Type") ?: "AIR") ?: Material.AIR
    val item = ItemStack(type)

    // 基本属性
    config.getInt("$path.Data", 0).let { item.durability = it.toShort() }
    val meta = item.itemMeta ?: return item
    config.getString("$path.Display")?.let { meta.displayName = it.colored() }
    config.getStringList("$path.Lore")?.let { meta.lore = it.colored() }

    // 附魔属性
    if (config.getBoolean("$path.Options.Glow", false)) {
        config.getConfigurationSection("$path.Enchantments")?.let { enchantments ->
            enchantments.getKeys(false).forEach { enchantmentKey ->
                val enchantment = Enchantment.getByName(enchantmentKey)
                val level = enchantments.getInt(enchantmentKey)
                if (enchantment != null) {
                    meta.addEnchant(enchantment, level, true)
                }
            }
        }
    }

    // 常规物品设置
    if (config.getBoolean("$path.Options.Unbreakable", false)) {
        meta.isUnbreakable = true
    }
    config.getStringList("$path.Options.HideFlags")?.mapNotNull { ItemFlag.valueOf(it) }
        ?.let { meta.addItemFlags(*it.toTypedArray()) }

    // 特殊物品设置
    when (meta) {
        is BannerMeta -> {
            config.getStringList("$path.Options.BannerPatterns")?.forEach { pattern ->
                val (colorName, patternName) = pattern.split("-", limit = 2)
                val color = DyeColor.valueOf(colorName)
                val bannerPattern = Pattern(color, PatternType.getByIdentifier(patternName)!!)
                meta.addPattern(bannerPattern)
            }
        }

        is LeatherArmorMeta -> {
            config.getInt("$path.Options.Color").let { colorInt ->
                meta.color = Color.fromRGB(colorInt)
            }
        }

        is PotionMeta -> {
            config.getString("$path.Options.BasePotionData.Type")?.let { potionTypeName ->
                val potionType = PotionType.valueOf(potionTypeName)
                val isExtended = config.getBoolean("$path.Options.BasePotionData.Extended")
                val isUpgraded = config.getBoolean("$path.Options.BasePotionData.Upgraded")
                val potionData = PotionData(potionType, isExtended, isUpgraded)
                meta.basePotionData = potionData
            }
            config.getStringList("$path.Options.PotionEffects")?.forEach { effect ->
                val (typeName, amplifier, duration) = effect.split("-", limit = 3)
                val potionEffectType = PotionEffectType.getByName(typeName)
                if (potionEffectType != null) {
                    meta.addCustomEffect(PotionEffect(potionEffectType, duration.toInt(), amplifier.toInt()), true)
                }
            }
        }

        is SkullMeta -> {
            config.getString("$path.Options.SkullOwner")?.let { owner ->
                meta.owner = owner
            }
        }
    }

    item.itemMeta = meta

    config.getConfigurationSection("$path.NBT")?.let { nbtSection ->
        item.itemTagReader {
            nbtSection.getKeys(true).forEach { key ->
                val value = nbtSection.get(key)
                set(key, value)
            }
            write(item)
        }
    }

    return item
}