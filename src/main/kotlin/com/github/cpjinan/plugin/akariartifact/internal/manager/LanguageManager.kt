package com.github.cpjinan.plugin.akariartifact.internal.manager

import com.github.cpjinan.plugin.akariartifact.AkariArtifact.plugin
import com.github.cpjinan.plugin.akariartifact.utils.ConfigUtil.saveDefaultResource
import com.github.cpjinan.plugin.akariartifact.utils.FileUtil
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText
import java.io.File

object LanguageManager {
    val lang = console().asLangText("Language")
    fun saveDefaultResource() {
        plugin.saveDefaultResource(
            "settings_${lang}.yml",
            File(FileUtil.dataFolder, "settings.yml")
        )
        plugin.saveDefaultResource(
            "item/Example.yml",
            File(FileUtil.dataFolder, "item/Example.yml")
        )
    }
}