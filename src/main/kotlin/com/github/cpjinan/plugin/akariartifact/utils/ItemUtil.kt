package com.github.cpjinan.plugin.akariartifact.utils

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

object ItemUtil {
    /**
     * 保存物品至配置文件
     * @param item 物品
     * @param file 配置文件
     * @param path 配置项路径
     * @author CPJiNan
     */
    fun saveItem(item: ItemStack, file: File, path: String) {
        val config = YamlConfiguration.loadConfiguration(file)
        config.set(path, item)
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
        config.set(path, item)
        config.save(file)
    }

    /**
     * 读取配置文件中的物品
     * @param file 配置文件
     * @param path 配置项路径
     * @return 文件指定路径下的 ItemStack
     * @author CPJiNan
     */
    fun loadItem(file: File, path: String): ItemStack? {
        val config = YamlConfiguration.loadConfiguration(file)
        return config.getItemStack(path)
    }

    /**
     * 读取配置文件中的物品
     * @param file 配置文件路径 (插件目录为根目录)
     * @param path 配置项路径
     * @return 文件指定路径下的 ItemStack
     * @author CPJiNan
     */
    fun loadItem(file: String, path: String): ItemStack? {
        val config = YamlConfiguration.loadConfiguration(File(FileUtil.dataFolder, file))
        return config.getItemStack(path)
    }
}