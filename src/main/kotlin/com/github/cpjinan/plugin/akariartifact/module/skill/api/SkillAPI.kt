package com.github.cpjinan.plugin.akariartifact.module.skill.api

import com.github.cpjinan.plugin.akariartifact.core.common.script.kether.Kether.evalKether
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil
import com.github.cpjinan.plugin.akariartifact.core.utils.ConfigUtil.getConfigSections
import com.github.cpjinan.plugin.akariartifact.core.utils.FileUtil
import com.github.cpjinan.plugin.akariartifact.module.item.api.ItemAPI
import com.github.cpjinan.plugin.akariartifact.module.skill.common.SkillCooldown
import ink.ptms.um.Mythic
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getName
import taboolib.platform.util.buildItem
import java.io.File

object SkillAPI {
    private var skillFiles: ArrayList<File> = arrayListOf()
    private var skillSections: HashMap<String, ConfigurationSection> = hashMapOf()
    private var skillNames: ArrayList<String> = arrayListOf()
    private var skillConfig: YamlConfiguration = YamlConfiguration()

    init {
        reloadSkill()
    }

    /**
     * 获取物品是否绑定了指定技能
     * @author CPJiNan
     */
    fun isBindSkill(item: ItemStack, skill: String): Boolean {
        val itemList =
            skillSections[skill]?.getStringList("Bind.Item")?.mapNotNull { ItemAPI.getItem(it) } ?: emptyList()
        if (itemList.any { buildItem(item) { amount = 1 } == buildItem(it) { amount = 1 } }) return true

        val nameList = skillSections[skill]?.getStringList("Bind.Name") ?: emptyList()
        if (nameList.any {
                when {
                    it.startsWith("Contain<=>") -> item.getName().contains(it.split("Contain<=>", limit = 2)[1])
                    it.startsWith("Equal<=>") -> item.getName() == it.split("Equal<=>", limit = 2)[1]
                    else -> false
                }
            }) return true

        val loreList = skillSections[skill]?.getStringList("Bind.Lore") ?: emptyList()
        if (loreList.any { loreBinding ->
                when {
                    loreBinding.startsWith("Contain<=>") -> {
                        val lore = loreBinding.split("Contain<=>", limit = 2)[1]
                        item.itemMeta?.lore?.any { it.contains(lore) } == true
                    }

                    loreBinding.startsWith("Equal<=>") -> {
                        val lore = loreBinding.split("Equal<=>", limit = 2)[1]
                        item.itemMeta?.lore?.any { it == lore } == true
                    }

                    else -> false
                }
            }) return true

        return false
    }

    /**
     * 玩家释放技能
     * @author CPJiNan
     */
    fun castSkill(player: Player, skill: String): Boolean {
        if (!isMetSkillCondition(player, skill)) return false
        if (!isSkillCooldownFinish(player, skill)) return false
        runSkillAction(player = player, skill = skill)
        return true
    }

    /**
     * 获取玩家是否满足技能释放条件
     * @author CPJiNan
     */
    fun isMetSkillCondition(player: Player, skill: String): Boolean {
        return skillSections[skill]?.getStringList("Condition")
            ?.all { it.evalKether(player).toString().toBoolean() } != false
    }

    /**
     * 获取玩家技能冷却是否结束
     * @author CPJiNan
     */
    fun isSkillCooldownFinish(player: Player, skill: String): Boolean {
        val cooldownID = skillSections[skill]?.getString("Cooldown")?.replace("Player", player.name) ?: return true
        return SkillCooldown.isCooldownFinish(cooldownID)
    }

    /**
     * 获取玩家技能冷却
     * @author CPJiNan
     */
    fun getSkillCooldown(player: Player, skill: String): Long {
        val cooldownID = skillSections[skill]?.getString("Cooldown")?.replace("Player", player.name) ?: return 0
        return SkillCooldown.getCooldown(cooldownID)
    }

    /**
     * 设置技能冷却时间
     * @author CPJiNan
     */
    fun setSkillCooldown(player: Player, skill: String, time: Long) {
        val cooldownID = skillSections[skill]?.getString("Cooldown")?.replace("Player", player.name) ?: return
        SkillCooldown.setCooldown(cooldownID, time)
    }

    /**
     * 增加技能冷却时间
     * @author CPJiNan
     */
    fun addSkillCooldown(player: Player, skill: String, time: Long) {
        val cooldownID = skillSections[skill]?.getString("Cooldown")?.replace("Player", player.name) ?: return
        SkillCooldown.addCooldown(cooldownID, time)
    }

    /**
     * 减少技能冷却时间
     * @author CPJiNan
     */
    fun removeSkillCooldown(player: Player, skill: String, time: Long) {
        val cooldownID = skillSections[skill]?.getString("Cooldown")?.replace("Player", player.name) ?: return
        SkillCooldown.removeCooldown(cooldownID, time)
    }

    /**
     * 执行技能动作
     * @author CPJiNan
     */
    fun runSkillAction(player: Player, skill: String) {
        runSkillAction(
            player = player,
            action = skillSections[skill]?.getStringList("Action") ?: emptyList()
        )
    }

    /**
     * 执行技能动作
     * @author CPJiNan
     */
    fun runSkillAction(player: Player, action: List<String>) {
        val skillMap: LinkedHashMap<String, MutableList<String>> = LinkedHashMap()

        action.forEach { entry ->
            when {
                entry.startsWith("Kether<=>") -> {
                    val skillContent = entry.split("Kether<=>", limit = 2)[1]
                    skillMap.getOrPut("Kether") { mutableListOf() }.add(skillContent)
                }

                entry.startsWith("MythicMobs<=>") -> {
                    val skillContent = entry.split("MythicMobs<=>", limit = 2)[1]
                    skillMap.getOrPut("MythicMobs") { mutableListOf() }.add(skillContent)
                }
            }
        }

        skillMap["Kether"]?.forEach { ketherSkill ->
            ketherSkill.evalKether(player)
        }

        skillMap["MythicMobs"]?.forEach { mythicSkill ->
            Mythic.API.castSkill(player, mythicSkill)
        }
    }

    /**
     * 重载技能配置文件
     * @author CPJiNan
     */
    fun reloadSkill() {
        skillFiles = FileUtil.getFile(File(FileUtil.dataFolder, "module/skill"), true)
            .filter { it.name.endsWith(".yml") }.toCollection(ArrayList())
        skillSections = skillFiles.getConfigSections()
        skillNames = skillSections.map { it.key }.toCollection(ArrayList())
        skillConfig = ConfigUtil.getMergedConfig(skillSections)
    }

    /**
     * 获取所有技能的配置文件
     * @return 技能配置文件列表
     * @author CPJiNan
     */
    fun getSkillFiles(): ArrayList<File> = skillFiles

    /**
     * 获取所有技能的配置节点
     * @return 技能配置节点列表 (由 技能ID 及其 配置节点 组成)
     * @author CPJiNan
     */
    fun getSkillSections(): HashMap<String, ConfigurationSection> = skillSections

    /**
     * 获取所有技能的名称
     * @return 宝石名称列表
     * @author CPJiNan
     */
    fun getSkillNames(): ArrayList<String> = skillNames

    /**
     * 获取所有技能配置合并后的新配置
     * @return 技能配置
     * @author CPJiNan
     */
    fun getSkillConfig(): YamlConfiguration = skillConfig
}