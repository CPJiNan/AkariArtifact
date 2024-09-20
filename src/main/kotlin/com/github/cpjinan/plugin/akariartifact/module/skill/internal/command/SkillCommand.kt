package com.github.cpjinan.plugin.akariartifact.module.skill.internal.command

import taboolib.common.platform.command.subCommand

@Suppress("DEPRECATION")
object SkillCommand {
    val skill = subCommand {
        literal("cast").dynamic("skill") {

        }

        literal("cooldown") {

        }
    }
}