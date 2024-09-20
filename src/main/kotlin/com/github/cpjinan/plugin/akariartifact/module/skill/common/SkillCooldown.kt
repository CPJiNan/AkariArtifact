package com.github.cpjinan.plugin.akariartifact.module.skill.common


object SkillCooldown {
    private val cooldownData = HashMap<String, Long>()

    /**
     * 设置冷却
     * @param id 组冷却 ID
     * @param time 冷却时间 (秒)
     */
    fun setCooldown(id: String, time: Long) {
        cooldownData[id] = System.currentTimeMillis() + time * 1000
    }

    /**
     * 获取组冷却是否冷却结束
     * @param id 组冷却 ID
     * @return 组冷却是否冷却结束
     */
    fun isCooldownFinish(id: String): Boolean {
        val endTime = cooldownData[id] ?: return true
        return System.currentTimeMillis() > endTime
    }

    /**
     * 获取组冷却的冷却剩余时间
     * @param id 组冷却 ID
     * @return 冷却时间 (秒)
     */
    fun getCooldown(id: String): Long {
        val endTime = cooldownData[id] ?: return 0
        val cooldown = endTime - System.currentTimeMillis()
        return if (cooldown > 0) cooldown / 1000 else 0
    }
}