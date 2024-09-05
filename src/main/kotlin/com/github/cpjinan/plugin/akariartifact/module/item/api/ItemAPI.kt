package com.github.cpjinan.plugin.akariartifact.module.item.api

import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import github.saukiya.sxattribute.SXAttribute
import ink.ptms.um.Mythic
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import taboolib.module.nms.getName
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
        saveItemToConfig(item, config, path)
        config.save(File(FileUtil.dataFolder, file))
    }

//    /**
//     * 读取配置文件中的物品
//     * @param file 配置文件
//     * @param path 配置项路径
//     * @return 文件指定路径下的 ItemStack
//     * @author CPJiNan
//     */
//    fun getItem(file: File, path: String): ItemStack? {
//        val config = YamlConfiguration.loadConfiguration(file)
//        val item = buildItem(Material.getMaterial(config.getString("$path.Type") ?: return null) ?: return null) {
//            name = config.getString("$path.Name")
//            lore.addAll(config.getStringList("$path.Lore"))
//            colored()
//        }
//        item.itemTagReader {
//            config.getConfigurationSection("$path.ItemTag")
//        }
//        return item
//    }
//
//    /**
//     * 读取配置文件中的物品
//     * @param file 配置文件路径 (插件目录为根目录)
//     * @param path 配置项路径
//     * @return 文件指定路径下的 ItemStack
//     * @author CPJiNan
//     */
//    fun getItem(file: String, path: String): ItemStack? {
//        val config = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, file))
//        val item = buildItem(Material.getMaterial(config.getString("$path.Type") ?: return null) ?: return null) {
//            name = config.getString("$path.Name")
//            lore.addAll(config.getStringList("$path.Lore"))
//            colored()
//        }
//        item.setItemTag(config.get("$path.ItemTag") as ItemTag)
//        return item
//    }

    /**
     * 读取外部插件中的物品
     * @param plugin 插件名称
     * @param key 物品索引
     * @return 插件下指定索引的 ItemStack
     * @author CPJiNan
     */
    fun getExternalItem(plugin: String, key: String): ItemStack? {
        var item: ItemStack? = null
        when (plugin) {
            "MythicMobs" -> item = Mythic.API.getItemStack(key)

            "SX-Attribute" -> item = SXAttribute.getApi().getItem(key, null)

            else -> throw IllegalArgumentException("Unable to find item $key in plugin $plugin.")
        }
        return item
    }

    @Suppress("DEPRECATION")
    private fun saveItemToConfig(item: ItemStack, config: YamlConfiguration, path: String) {
        val meta = item.itemMeta ?: return

        // 基本属性
        config.set("$path.Type", item.type.name)
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
        item.durability.takeIf { it != 0.toShort() }?.let { config.set("$path.Options.Durability", it.toInt()) }
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
                val effects = meta.customEffects.map { "${it.type.name}-${it.amplifier}-${it.duration}" }
                config.set("$path.Options.PotionEffects", effects)
            }
            // 头颅
            is SkullMeta -> {
                meta.owner?.let { config.set("$path.Options.SkullOwner", it) }
            }
        }
    }


}