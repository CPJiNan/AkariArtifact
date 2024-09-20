package com.github.cpjinan.plugin.akariartifact.module.skill.internal.listener

import com.github.cpjinan.plugin.akariartifact.core.common.script.kether.Kether.evalKether
import com.github.cpjinan.plugin.akariartifact.module.skill.ModuleSkill
import com.github.cpjinan.plugin.akariartifact.module.skill.api.SkillAPI
import com.github.cpjinan.plugin.akariartifact.module.skill.common.SkillCooldown
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendLang

object SkillListener {
    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!ModuleSkill.isEnabledModule()) return

        val player = event.player
        val item = event.item ?: return

        val skillName = SkillAPI.getSkillNames().firstOrNull { SkillAPI.isBindSkill(item, it) } ?: return
        val skillSection = SkillAPI.getSkillSections()[skillName] ?: return

        if (!skillSection.getStringList("Condition")
                .all { it.evalKether(player).toString().toBoolean() }
        ) {
            player.sendLang("Skill-Condition-Not-Met")
            return
        }

        if (!SkillCooldown.isCooldownFinish("${player.name}.$skillName")) {
            player.sendLang(
                "Skill-Cooldown",
                SkillCooldown.getCooldown("${player.name}.$skillName")
            )
            return
        }
    }
}