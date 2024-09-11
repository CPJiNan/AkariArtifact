package com.github.cpjinan.plugin.akariartifact.module.item.internal.command

import com.github.cpjinan.plugin.akariartifact.module.item.ModuleItem
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

object ItemCommand {
    val item = subCommand {
        if (!ModuleItem.isEnabledModule()) return@subCommand
        createHelper()

        literal("list") {

        }

        literal("get").dynamic("id").literal("amount") {

        }
    }
}