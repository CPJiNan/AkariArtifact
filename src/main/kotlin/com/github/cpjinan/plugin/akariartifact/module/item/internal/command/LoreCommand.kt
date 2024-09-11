package com.github.cpjinan.plugin.akariartifact.module.item.internal.command

import com.github.cpjinan.plugin.akariartifact.core.utils.CommandUtil
import com.github.cpjinan.plugin.akariartifact.module.item.ModuleItem
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.int
import taboolib.common.platform.command.subCommand
import taboolib.common5.util.replace
import taboolib.expansion.createHelper
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir
import taboolib.platform.util.modifyLore

object LoreCommand {
    private val clipboard: HashMap<Player, String> = hashMapOf()

    @CommandBody
    val lore = subCommand {
        if (!ModuleItem.isEnabledModule()) return@subCommand
        createHelper()
        literal("check") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                try {
                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        if (item.isNotAir()) {
                            sender.sendLang("Lore-Check")
                            item.itemMeta?.lore?.forEachIndexed { index, content ->
                                sender.sendMessage("&7${index + 1} &8| $content".colored())
                            }
                        } else sender.sendLang("Air-In-Hand")
                    }
                } catch (error: IndexOutOfBoundsException) {
                    sender.sendLang("Index-Out-Of-Bounds")
                }
            }
        }

        literal("add") {
            dynamic("lore") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    add(context["lore"].colored())
                                }
                                sender.sendLang("Lore-Add", context["lore"].colored())
                            } else sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        val lore = context["lore"] + CommandUtil.parseContentAfterSpace(content.split(" "))
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    add(lore.colored())
                                }
                                if (!silent) sender.sendLang("Lore-Add", lore.colored())
                            } else if (!silent) sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("remove") {
            int("line") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    removeAt(context.int("line") - 1)
                                }
                                sender.sendLang("Lore-Remove", context.int("line"))
                            } else sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    removeAt(context.int("line") - 1)
                                }
                                if (!silent) sender.sendLang("Lore-Remove", context.int("line"))
                            } else if (!silent) sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("set") {
            int("line").dynamic("lore") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    set(context.int("line") - 1, context["lore"].colored())
                                }
                                sender.sendLang("Lore-Set", context.int("line"), context["lore"].colored())
                            } else sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        val lore = context["lore"] + CommandUtil.parseContentAfterSpace(content.split(" "))
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    set(context.int("line") - 1, lore.colored())
                                }
                                if (!silent) sender.sendLang("Lore-Set", context.int("line"), lore.colored())
                            } else if (!silent) sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("insert") {
            int("line").dynamic("lore") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    add(context.int("line"), context["lore"].colored())
                                }
                                sender.sendLang(
                                    "Lore-Insert",
                                    context.int("line"),
                                    context.int("line") + 1,
                                    context["lore"].colored()
                                )
                            } else sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        val lore = context["lore"] + CommandUtil.parseContentAfterSpace(content.split(" "))
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    add(context.int("line"), lore.colored())
                                }
                                if (!silent) sender.sendLang(
                                    "Lore-Insert",
                                    context.int("line"),
                                    context.int("line") + 1,
                                    lore.colored()
                                )
                            } else if (!silent) sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("replace") {
            dynamic("oldChar").dynamic("newChar") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isAir()) {
                                sender.sendLang("Air-In-Hand")
                                return@execute
                            }

                            val meta = item.itemMeta
                            meta.lore =
                                item.itemMeta.lore.replace(Pair(context["oldChar"], context["newChar"]))

                            item.itemMeta = meta

                            sender.sendLang(
                                "Lore-Replace",
                                context["oldChar"],
                                context["newChar"],
                                "All"
                            )
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val args = CommandUtil.parseOptions(content.split(" "))
                            var silent = false
                            var line: Int? = null

                            for ((k, v) in args) {
                                when (k.lowercase()) {
                                    "silent" -> silent = true
                                    "line" -> line = v?.toInt()
                                }
                            }

                            val item = it?.inventory?.itemInMainHand
                            if (item.isAir()) {
                                sender.sendLang("Air-In-Hand")
                                return@execute
                            }

                            val meta = item.itemMeta

                            if (line == null) {
                                meta.lore =
                                    item.itemMeta.lore.replace(Pair(context["oldChar"], context["newChar"]))
                            } else {
                                meta.lore[line - 1] =
                                    item.itemMeta.lore[line - 1].replace(Pair(context["oldChar"], context["newChar"]))
                            }

                            item.itemMeta = meta

                            if (!silent) sender.sendLang(
                                "Lore-Replace",
                                context["oldChar"],
                                context["newChar"],
                                line ?: "All"
                            )
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("clear") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                try {
                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        if (item.isNotAir()) {
                            item.modifyLore {
                                clear()
                            }
                            sender.sendLang("Lore-Clear")
                        } else sender.sendLang("Air-In-Hand")
                    }
                } catch (error: IndexOutOfBoundsException) {
                    sender.sendLang("Index-Out-Of-Bounds")
                }
            }
        }.dynamic("options") {
            execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, content: String ->
                try {
                    val args = CommandUtil.parseOptions(content.split(" "))
                    var silent = false

                    for ((k, _) in args) {
                        when (k.lowercase()) {
                            "silent" -> silent = true
                        }
                    }

                    sender.castSafely<Player>().let {
                        val item = it?.inventory?.itemInMainHand
                        if (item.isNotAir()) {
                            item.modifyLore {
                                clear()
                            }
                            if (!silent) sender.sendLang("Lore-Clear")
                        } else if (!silent) sender.sendLang("Air-In-Hand")
                    }
                } catch (error: IndexOutOfBoundsException) {
                    sender.sendLang("Index-Out-Of-Bounds")
                }
            }
        }

        literal("clone") {
            int("lineA").int("lineB") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    set(context.int("lineB") - 1, get(context.int("lineA") - 1))
                                }
                                sender.sendLang("Lore-Clone", context.int("lineA"), context.int("lineB"))
                            } else sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    set(context.int("lineB") - 1, get(context.int("lineA") - 1))
                                }
                                if (!silent) sender.sendLang("Lore-Clone", context.int("lineA"), context.int("lineB"))
                            } else if (!silent) sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("copy") {
            int("line") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    val value = get(context.int("line") - 1)
                                    clipboard[it] = value
                                    sender.sendLang("Lore-Copy", context.int("line"), value)
                                }
                            } else sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    val value = get(context.int("line") - 1)
                                    clipboard[it] = value
                                    if (!silent) sender.sendLang("Lore-Copy", context.int("line"), value)
                                }
                            } else if (!silent) sender.sendLang("Air-In-Hand") else return@execute
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("paste") {
            int("line") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    sender.castSafely<Player>().let {
                        try {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    clipboard[it]?.colored()?.let { value ->
                                        if (clipboard.isNotEmpty()) {
                                            set(context.int("line") - 1, value)
                                            sender.sendLang("Lore-Paste", context.int("line"), value)
                                        } else sender.sendLang("Lore-Clipboard-Empty")
                                    }
                                }
                            } else sender.sendLang("Air-In-Hand")
                        } catch (error: IndexOutOfBoundsException) {
                            sender.sendLang("Index-Out-Of-Bounds")
                        }
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    clipboard[it]?.colored()?.let { value ->
                                        if (clipboard.isNotEmpty()) {
                                            set(context.int("line"), value)
                                            if (!silent) sender.sendLang("Lore-Paste", context.int("line"), value)
                                        } else if (!silent) sender.sendLang("Lore-Clipboard-Empty")
                                    }
                                }
                            } else if (!silent) sender.sendLang("Air-In-Hand") else return@execute
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("cut") {
            int("line") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                    try {
                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    val value = get(context.int("line") - 1)
                                    clipboard[it] = value
                                    removeAt(context.int("line") - 1)
                                    sender.sendLang("Lore-Cut", context.int("line"), value)
                                }
                            } else sender.sendLang("Air-In-Hand")
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                    try {
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            val item = it?.inventory?.itemInMainHand
                            if (item.isNotAir()) {
                                item.modifyLore {
                                    val value = get(context.int("line") - 1)
                                    clipboard[it] = value
                                    removeAt(context.int("line") - 1)
                                    if (!silent) sender.sendLang("Lore-Cut", context.int("line"), value)
                                }
                            } else if (!silent) sender.sendLang("Air-In-Hand") else return@execute
                        }
                    } catch (error: IndexOutOfBoundsException) {
                        sender.sendLang("Index-Out-Of-Bounds")
                    }
                }
            }
        }

        literal("clipboard") {

            literal("check") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                    sender.castSafely<Player>().let {
                        if (clipboard[it]?.isNotEmpty() == true) {
                            sender.sendLang(
                                "Lore-Clipboard-Check",
                                clipboard[it]!!
                            )
                        } else sender.sendLang("Lore-Clipboard-Empty")
                    }
                }
            }

            literal("set") {
                dynamic("value") {
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, _: String ->
                        sender.castSafely<Player>().let {
                            clipboard[it!!] = context["value"]
                        }
                        sender.sendLang("Lore-Clipboard-Set", context["value"])
                    }
                }.dynamic("options") {
                    execute<ProxyCommandSender> { sender: ProxyCommandSender, context: CommandContext<ProxyCommandSender>, content: String ->
                        val value = context["value"] + CommandUtil.parseContentAfterSpace(content.split(" "))
                        val args = CommandUtil.parseOptions(content.split(" "))
                        var silent = false

                        for ((k, _) in args) {
                            when (k.lowercase()) {
                                "silent" -> silent = true
                            }
                        }

                        sender.castSafely<Player>().let {
                            clipboard[it!!] = value
                        }

                        if (!silent) sender.sendLang("Lore-Clipboard-Set", value)
                    }
                }
            }

            literal("clear") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, _: String ->
                    sender.castSafely<Player>().let {
                        clipboard[it!!] = ""
                        sender.sendLang("Lore-Clipboard-Clear")
                    }
                }
            }.dynamic("options") {
                execute<ProxyCommandSender> { sender: ProxyCommandSender, _: CommandContext<ProxyCommandSender>, content: String ->
                    val args = CommandUtil.parseOptions(content.split(" "))
                    var silent = false

                    for ((k, _) in args) {
                        when (k.lowercase()) {
                            "silent" -> silent = true
                        }
                    }

                    sender.castSafely<Player>().let {
                        clipboard[it!!] = ""
                    }

                    if (!silent) sender.sendLang("Lore-Clipboard-Clear")
                }
            }

        }
    }
}