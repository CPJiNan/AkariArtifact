package com.github.cpjinan.plugin.akariartifact.utils

object RegexUtil {
    fun String.matches(regex: String): Boolean {
        return regex.toRegex().matches(this)
    }
}