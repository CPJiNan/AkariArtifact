package com.github.cpjinan.plugin.akariartifact.module.skill.internal.listener

import com.github.cpjinan.plugin.akariartifact.module.skill.ModuleSkill
import com.github.cpjinan.plugin.akariartifact.module.skill.api.SkillAPI
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendLang

object SkillListener {
    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!ModuleSkill.isEnabledModule()) return

        val player = event.player
        val item = event.item ?: return

        val skillNames = SkillAPI.getSkillNames()
            .filter { SkillAPI.isBindSkill(item, it) }
            .filter {
                val isSneak = player.isSneaking
                val actionType = SkillAPI.getSkillSections()[it]?.getStringList("Type") ?: return
                actionType.any { type ->
                    when (type) {
                        "Left-Click-Air" -> event.action == Action.LEFT_CLICK_AIR && !isSneak
                        "Left-Click-Block" -> event.action == Action.LEFT_CLICK_BLOCK && !isSneak
                        "Right-Click-Air" -> event.action == Action.RIGHT_CLICK_AIR && !isSneak
                        "Right-Click-Block" -> event.action == Action.RIGHT_CLICK_BLOCK && !isSneak
                        "Shift-Left-Click-Air" -> event.action == Action.LEFT_CLICK_AIR && isSneak
                        "Shift-Left-Click-Block" -> event.action == Action.LEFT_CLICK_BLOCK && isSneak
                        "Shift-Right-Click-Air" -> event.action == Action.RIGHT_CLICK_AIR && isSneak
                        "Shift-Right-Click-Block" -> event.action == Action.RIGHT_CLICK_BLOCK && isSneak
                        else -> return
                    }
                }
            }

        skillNames.forEach { skillName ->
            if (!SkillAPI.isMetSkillCondition(player, skillName)) {
                player.sendLang(
                    "Skill-Condition-Not-Met",
                    SkillAPI.getSkillSections()[skillName]?.getString("Display") ?: skillName
                )
                return
            }

            if (!SkillAPI.isSkillCooldownFinish(player, skillName)) {
                player.sendLang(
                    "Skill-Cooldown-Check",
                    player.name,
                    SkillAPI.getSkillSections()[skillName]?.getString("Display") ?: skillName,
                    SkillAPI.getSkillCooldown(player, skillName)
                )
                return
            }

            SkillAPI.runSkillAction(player, skillName)
        }
    }
}