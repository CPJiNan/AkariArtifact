package com.github.cpjinan.plugin.akariartifact.api

import com.github.cpjinan.plugin.akariartifact.utils.FileUtil
import github.saukiya.sxattribute.SXAttribute
import ink.ptms.um.Mythic
import io.rokuko.azureflow.features.item.factory.AzureFlowItemFactoryService
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import taboolib.module.nms.*
import taboolib.platform.util.buildItem
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

            "AzureFlow" -> {
                AzureFlowItemFactoryService.INSTANCE[key].let {
                    item = buildItem(Material.valueOf(it?.material!!)) {
                        name = it.name
                        lore.addAll(it.lore.orEmpty())
                        colored()
                    }
                }
            }

            else -> throw IllegalArgumentException("Unable to find item $key in plugin $plugin.")
        }
        return item
    }

    private fun saveItemToConfig(item: ItemStack, config: YamlConfiguration, path: String) {
        item.itemMeta?.let {
            config.set("$path.Type", item.type.name)
            config.set("$path.Display", item.getName())
            if (it.hasLore()) config.set("$path.Lore", it.lore)
            if (it.hasEnchants()) it.enchants.forEach { (enchantment, level) ->
                config.set("$path.Enchantments.${enchantment.key.key}", level)
            }
            if (item.durability.toInt() != 0) config.set("$path.Options.Damage", item.durability.toInt())
            if (item.itemMeta?.isUnbreakable != false) config.set("$path.Options.Unbreakable", item.itemMeta?.isUnbreakable)
            if (it.hasEnchants()) config.set("$path.Options.Glow", it.hasEnchants())
            if (it.itemFlags.map { flag -> flag.name }.isNotEmpty()) config.set("$path.Options.HideFlags", it.itemFlags.map { flag -> flag.name })
        }

        if (item.itemMeta is BannerMeta) {
            val bannerMeta = item.itemMeta as BannerMeta
            val patterns = bannerMeta.patterns.map { "${it.color.name}-${it.pattern.identifier}" }
            config.set("$path.Options.BannerPatterns", patterns)
        }
        if (item.itemMeta is LeatherArmorMeta) {
            val leatherMeta = item.itemMeta as LeatherArmorMeta
            config.set("$path.Options.Color", leatherMeta.color.asRGB())
        }
        if (item.itemMeta is PotionMeta) {
            val potionMeta = item.itemMeta as PotionMeta
            config.set("$path.Options.BasePotionData", potionMeta.basePotionData.type.name)
            val effects = potionMeta.customEffects.map { "${it.type.name}-${it.amplifier}-${it.duration}" }
            config.set("$path.Options.PotionEffects", effects)
        }
        if (item.itemMeta is SpawnEggMeta) {
            val spawnEggMeta = item.itemMeta as SpawnEggMeta
            config.set("$path.Options.EntityType", spawnEggMeta.spawnedType.name)
        }
        if (item.itemMeta is SkullMeta) {
            val skullMeta = item.itemMeta as SkullMeta
            skullMeta.owningPlayer?.let { player ->
                config.set("$path.Options.SkullOwner", player.name)
            }
            if (skullMeta.hasOwner()) {
                skullMeta.ownerProfile?.textures?.let { textures ->
                    config.set("$path.Options.SkullTexture", textures.toString())
                }
            }
        }
    }

}