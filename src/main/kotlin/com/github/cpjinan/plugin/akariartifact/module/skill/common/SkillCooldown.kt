package com.github.cpjinan.plugin.akariartifact.module.skill.common

import java.util.UUID

object SkillCooldown {
    private val cooldown = HashMap<UUID, HashMap<String, Long>>()

    /**
     * 为冷却组设置冷却
     * @param player 玩家 UUID
     * @param cooldownID 冷却组 ID
     * @param cooldownTime 冷却时间 (秒)
     */
    fun setCooldown(player: UUID, cooldownID: String, cooldownTime: Long) {
        val playerCooldowns = cooldown.getOrPut(player) { hashMapOf() }
        playerCooldowns[cooldownID] = System.currentTimeMillis() + cooldownTime * 1000
    }

    /**
     * 获取冷却组是否冷却结束
     * @param player 玩家 UUID
     * @param cooldownID 冷却组 ID
     * @return 冷却组是否冷却结束
     */
    fun isCooldownFinish(player: UUID, cooldownID: String): Boolean {
        val playerCooldown = cooldown[player] ?: return true
        val endTime = playerCooldown[cooldownID] ?: return true
        return System.currentTimeMillis() > endTime
    }

    /**
     * 获取冷却组的冷却剩余时间
     * @param player 玩家UUID
     * @param cooldownID 冷却组 ID
     * @return 冷却时间 (秒)
     */
    fun getCooldown(player: UUID, cooldownID: String): Long {
        val playerCooldown = cooldown[player] ?: return 0
        val endTime = playerCooldown[cooldownID] ?: return 0
        val remainingTime = endTime - System.currentTimeMillis()
        return if (remainingTime > 0) remainingTime / 1000 else 0
    }
}