package com.github.cpjinan.plugin.akariartifact.module.skill.internal.command

import com.github.cpjinan.plugin.akariartifact.core.utils.CommandUtil
import com.github.cpjinan.plugin.akariartifact.module.skill.api.SkillAPI
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.command.suggestPlayers
import taboolib.module.lang.sendLang
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

@Suppress("DEPRECATION")
object SkillCommand {
    val skill = subCommand {
        literal("cast").player("player") { suggestPlayers() }.dynamic("skill") {
            suggest { SkillAPI.getSkillNames() }
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                val player = context.player("player").cast<Player>()
                val skill = context["skill"]
                SkillAPI.castSkill(player, skill)
            }
        }.dynamic("options", optional = true) {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                val args = CommandUtil.parseOptions(content.split(" "))
                var force = false

                for ((k, _) in args) {
                    when (k.lowercase()) {
                        "force" -> force = true
                    }
                }

                val player = context.player("player").cast<Player>()
                val skill = context["skill"]

                when (force) {
                    true -> SkillAPI.runSkillAction(player = player, skill = skill)
                    false -> SkillAPI.castSkill(player, skill)
                }
            }
        }

        literal("cooldown") {
            literal("check").player("player") { suggestPlayers() }.dynamic("skill") {
                suggest { SkillAPI.getSkillNames() }
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    val player = context.player("player").cast<Player>()
                    val skill = context["skill"]

                    when {
                        SkillAPI.getSkillCooldown(player, skill) == 0L ->
                            sender.sendLang(
                                "Skill-Ready",
                                SkillAPI.getSkillSections()[skill]?.getString("Display") ?: skill
                            )

                        else -> sender.sendLang(
                            "Skill-Cooldown-Check",
                            player.name,
                            SkillAPI.getSkillSections()[skill]?.getString("Display") ?: skill,
                            SkillAPI.getSkillCooldown(player, skill)
                        )
                    }
                }
            }

            literal("set").player("player") { suggestPlayers() }
                .dynamic("skill") { suggest { SkillAPI.getSkillNames() } }.dynamic("time") {
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                        val player = context.player("player").cast<Player>()
                        val skill = context["skill"]
                        val time = context["time"].toLong()
                        SkillAPI.setSkillCooldown(player, skill, time)
                        sender.sendLang(
                            "Skill-Cooldown-Set",
                            player.name,
                            SkillAPI.getSkillSections()[skill]?.getString("Display") ?: skill,
                            time
                        )
                    }
                }.dynamic("options") {
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        val player = context.player("player").cast<Player>()
                        val skill = context["skill"]
                        val time = context["time"].toLong()

                        SkillAPI.setSkillCooldown(player, skill, time)

                        if (!silent) sender.sendLang(
                            "Skill-Cooldown-Set",
                            player.name,
                            SkillAPI.getSkillSections()[skill]?.getString("Display") ?: skill,
                            time
                        )
                    }
                }

            literal("add").player("player") { suggestPlayers() }
                .dynamic("skill") { suggest { SkillAPI.getSkillNames() } }.dynamic("time") {
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                        val player = context.player("player").cast<Player>()
                        val skill = context["skill"]
                        val time = context["time"].toLong()
                        SkillAPI.addSkillCooldown(player, skill, time)
                        sender.sendLang(
                            "Skill-Cooldown-Add",
                            player.name,
                            SkillAPI.getSkillSections()[skill]?.getString("Display") ?: skill,
                            time
                        )
                    }
                }.dynamic("options") {
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        val player = context.player("player").cast<Player>()
                        val skill = context["skill"]
                        val time = context["time"].toLong()

                        SkillAPI.addSkillCooldown(player, skill, time)

                        if (!silent) sender.sendLang(
                            "Skill-Cooldown-Add",
                            player.name,
                            SkillAPI.getSkillSections()[skill]?.getString("Display") ?: skill,
                            time
                        )
                    }
                }

            literal("remove").player("player") { suggestPlayers() }
                .dynamic("skill") { suggest { SkillAPI.getSkillNames() } }.dynamic("time") {
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                        val player = context.player("player").cast<Player>()
                        val skill = context["skill"]
                        val time = context["time"].toLong()
                        SkillAPI.removeSkillCooldown(player, skill, time)
                        sender.sendLang(
                            "Skill-Cooldown-Remove",
                            player.name,
                            SkillAPI.getSkillSections()[skill]?.getString("Display") ?: skill,
                            time
                        )
                    }
                }.dynamic("options") {
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        val player = context.player("player").cast<Player>()
                        val skill = context["skill"]
                        val time = context["time"].toLong()

                        SkillAPI.removeSkillCooldown(player, skill, time)

                        if (!silent) sender.sendLang(
                            "Skill-Cooldown-Remove",
                            player.name,
                            SkillAPI.getSkillSections()[skill]?.getString("Display") ?: skill,
                            time
                        )
                    }
                }
        }
    }
}