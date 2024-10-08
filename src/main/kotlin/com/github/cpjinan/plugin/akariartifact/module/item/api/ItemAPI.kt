package com.github.cpjinan.plugin.akariartifact.module.item.api

import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.getConfigSections
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import github.saukiya.sxattribute.SXAttribute
import ink.ptms.um.Mythic
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.configuration.ConfigurationSection
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
import pers.neige.neigeitems.manager.ItemManager
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.*
import taboolib.platform.util.buildItem
import java.io.File

object ItemAPI {
    private var itemFiles: ArrayList<File> = arrayListOf()
    private var itemSections: HashMap<String, ConfigurationSection> = hashMapOf()
    private var itemNames: ArrayList<String> = arrayListOf()
    private var itemConfig: YamlConfiguration = YamlConfiguration()

    init {
        reloadItem()
    }

    /**
     * 保存物品至配置文件
     * @param item 物品
     * @param file 配置文件
     * @param id 物品 ID
     * @author CPJiNan
     */
    fun saveItem(item: ItemStack, file: File, id: String) {
        val config = YamlConfiguration.loadConfiguration(file)
        config.set(id, null)
        saveItemToConfig(item, config, id)
        config.save(file)
    }

    /**
     * 保存物品至配置文件
     * @param item 物品
     * @param file 配置文件路径 (插件目录为根目录)
     * @param id 物品 ID
     * @author CPJiNan
     */
    fun saveItem(item: ItemStack, file: String, id: String) {
        val itemFile = FileUtil.getFileOrCreate(file)
        val config = YamlConfiguration.loadConfiguration(itemFile)
        config.set(id, null)
        saveItemToConfig(item, config, id)
        config.save(itemFile)
    }

    /**
     * 从配置文件获取物品
     * @param id 物品 ID
     * @return 指定物品的 ItemStack
     * @author CPJiNan
     */
    fun getItem(id: String): ItemStack? {
        return getItemFromConfig(itemConfig, id)
    }

    /**
     * 从配置文件获取物品
     * @param file 配置文件
     * @param id 物品 ID
     * @return 指定物品的 ItemStack
     * @author CPJiNan
     */
    fun getItem(file: File, id: String): ItemStack? {
        val config = YamlConfiguration.loadConfiguration(file)
        return getItemFromConfig(config, id)
    }

    /**
     * 从配置文件获取物品
     * @param file 配置文件路径 (插件目录为根目录)
     * @param id 物品 ID
     * @return 指定物品的 ItemStack
     * @author CPJiNan
     */
    fun getItem(file: String, id: String): ItemStack? {
        val config = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, file))
        return getItemFromConfig(config, id)
    }

    /**
     * 从配置文件获取物品
     * @param config 配置文件
     * @param id 物品 ID
     * @return 指定物品的 ItemStack
     * @author CPJiNan
     */
    fun getItem(config: YamlConfiguration, id: String): ItemStack? {
        return getItemFromConfig(config, id)
    }

    /**
     * 从其他插件获取物品
     * @param plugin 插件名称
     * @param id 物品 ID
     * @param player 执行玩家 (默认为 null)
     * @return 指定物品的 ItemStack
     * @author CPJiNan
     */
    fun getItem(plugin: String, id: String, player: Player? = null): ItemStack? {
        return getItemFromPlugin(plugin, id, player)
    }

    /**
     * 重载物品配置文件
     * @author CPJiNan
     */
    fun reloadItem() {
        itemFiles = FileUtil.getFile(File(FileUtil.dataFolder, "module/item"), true)
            .filter { it.name.endsWith(".yml") }.toCollection(ArrayList())
        itemSections = itemFiles.getConfigSections()
        itemNames = itemSections.map { it.key }.toCollection(ArrayList())
        itemConfig = ConfigUtil.getMergedConfig(itemSections)
    }

    /**
     * 获取所有物品的配置文件
     * @return 物品配置文件列表
     * @author CPJiNan
     */
    fun getItemFiles(): ArrayList<File> = itemFiles

    /**
     * 获取所有物品的配置节点
     * @return 物品配置节点列表 (由 物品ID 及其 配置节点 组成)
     * @author CPJiNan
     */
    fun getItemSections(): HashMap<String, ConfigurationSection> = itemSections

    /**
     * 获取所有物品的名称
     * @return 物品名称列表
     * @author CPJiNan
     */
    fun getItemNames(): ArrayList<String> =
        itemNames.filter { itemSections[it]?.getBoolean("Hide") != true }.toCollection(ArrayList())

    /**
     * 获取所有物品配置合并后的新配置
     * @return 物品配置
     * @author CPJiNan
     */
    fun getItemConfig(): YamlConfiguration = itemConfig

    private fun saveItemToConfig(item: ItemStack, config: YamlConfiguration, path: String) {
        buildItem(item) {
            // 基本属性
            config.set("$path.Type", XMaterial.matchXMaterial(material).name)
            damage.takeIf { it != 0 }?.let { config.set("$path.Data", it) }
            config.set("$path.Display", item.getName())
            lore.takeIf { it.isNotEmpty() }.let { config.set("$path.Lore", it) }

            // 附魔属性
            if (enchants.isNotEmpty()) {
                config.set("$path.Options.Glow", true)
                enchants.forEach { (enchantment, level) ->
                    config.set("$path.Enchantments.${enchantment.name}", level)
                }
            }

            // 常规物品设置
            isUnbreakable.takeIf { it }?.let { config.set("$path.Options.Unbreakable", it) }
            flags.takeIf { it.isNotEmpty() }?.map { it.name }
                ?.let { config.set("$path.Options.HideFlags", it) }

            // 特殊物品设置
            when (originMeta) {
                // 旗帜
                is BannerMeta -> {
                    val patterns = patterns.map { "${it.color.name}-${it.pattern.identifier}" }
                    config.set("$path.Options.BannerPatterns", patterns)
                }
                // 皮革护甲
                is LeatherArmorMeta -> config.set("$path.Options.Color", color?.asRGB() ?: Color.WHITE)
                // 药水
                is PotionMeta -> {
                    val potionTypeName = potionData?.type?.name
                    val isExtended = potionData?.isExtended
                    val isUpgraded = potionData?.isUpgraded
                    val effects = potions.map { "${it.type.name}-${it.amplifier}-${it.duration}" }
                    config.set("$path.Options.BasePotionData.Type", potionTypeName)
                    config.set("$path.Options.BasePotionData.Extended", isExtended)
                    config.set("$path.Options.BasePotionData.Upgraded", isUpgraded)
                    config.set("$path.Options.PotionEffects", effects)
                }
                // 头颅
                is SkullMeta -> {
                    skullOwner?.let { config.set("$path.Options.SkullOwner", it) }
                }
            }

            // NBT
            val itemTag = item.getItemTag()
            itemTag.entries.forEach { tag ->
                if (tag.key !in listOf(
                        "display",
                        "Damage",
                        "ench", "Enchantments",
                        "Unbreakable",
                        "HideFlags",
                        "BlockEntityTag",
                        "Potion",
                        "SkullOwner"
                    )
                ) {
                    fun getItemTagData(itemTagData: ItemTagData): Any {
                        return when (val data = itemTagData.unsafeData()) {
                            is ItemTag -> {
                                val compoundData = mutableMapOf<Any, Any>()
                                data.entries.forEach { entry ->
                                    compoundData[entry.key] = getItemTagData(entry.value)
                                }
                                compoundData
                            }

                            is ItemTagList -> {
                                val listData = mutableListOf<Any>()
                                data.forEach { listData.add(getItemTagData(it)) }
                                listData
                            }

                            else -> data
                        }
                    }

                    config.set("$path.NBT.${tag.key}", getItemTagData(tag.value))
                }
            }
        }
    }

    private fun getItemFromConfig(config: YamlConfiguration, path: String): ItemStack? {
        // 从其他插件获取物品
        val plugin = config.getString("$path.Plugin")
        val id = config.getString("$path.ID")
        if (plugin in listOf("MythicMobs", "SX-Attribute")) return getItemFromPlugin(plugin = plugin, id = id)

        // 从配置文件获取物品
        val type = XMaterial.valueOf(config.getString("$path.Type") ?: return null)
        val item = buildItem(type) {
            // 基本属性
            config.getInt("$path.Data", 0).let { damage = it }
            config.getString("$path.Display")?.let { name = it }
            config.getStringList("$path.Lore")?.let { lore.addAll(it) }

            // 附魔属性
            config.getConfigurationSection("$path.Enchantments")?.let { enchantments ->
                enchantments.getKeys(false).forEach { enchantmentKey ->
                    val enchantment = Enchantment.getByName(enchantmentKey)
                    val level = enchantments.getInt(enchantmentKey)
                    if (enchantment != null) {
                        enchants[enchantment] = level
                    }
                }
            }

            // 常规物品设置
            if (config.getBoolean("$path.Options.Unbreakable", false)) {
                isUnbreakable = true
            }
            config.getStringList("$path.Options.HideFlags")?.mapNotNull { ItemFlag.valueOf(it) }
                ?.let { flags.addAll(it) }

            // 特殊物品设置
            when (originMeta) {
                is BannerMeta -> {
                    config.getStringList("$path.Options.BannerPatterns")?.forEach { pattern ->
                        val (colorName, patternName) = pattern.split("-", limit = 2)
                        val color = DyeColor.valueOf(colorName)
                        val bannerPattern = Pattern(color, PatternType.getByIdentifier(patternName)!!)
                        patterns.add(bannerPattern)
                    }
                }

                is LeatherArmorMeta -> {
                    config.getInt("$path.Options.Color").let { colorInt ->
                        color = Color.fromRGB(colorInt)
                    }
                }

                is PotionMeta -> {
                    config.getString("$path.Options.BasePotionData.Type")?.let { potionTypeName ->
                        val potionType = PotionType.valueOf(potionTypeName)
                        val isExtended = config.getBoolean("$path.Options.BasePotionData.Extended")
                        val isUpgraded = config.getBoolean("$path.Options.BasePotionData.Upgraded")
                        val basePotionData = PotionData(potionType, isExtended, isUpgraded)
                        potionData = basePotionData
                    }
                    config.getStringList("$path.Options.PotionEffects")?.forEach { effect ->
                        val (typeName, amplifier, duration) = effect.split("-", limit = 3)
                        val potionEffectType = PotionEffectType.getByName(typeName)
                        if (potionEffectType != null) {
                            potions.add(PotionEffect(potionEffectType, duration.toInt(), amplifier.toInt()))
                        }
                    }
                }

                is SkullMeta -> {
                    config.getString("$path.Options.SkullOwner")?.let { owner ->
                        skullOwner = owner
                    }
                }
            }

            // 名称 Lore 上色
            colored()
        }

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

    private fun getItemFromPlugin(plugin: String, id: String, player: Player? = null): ItemStack? {
        val item: ItemStack? = when (plugin) {
            "MythicMobs" -> Mythic.API.getItemStack(id)

            "SX-Attribute" -> SXAttribute.getApi().getItem(id, player)

            "NeigeItems" -> ItemManager.getItemStack(id)

            else -> null
        }
        return item
    }
}