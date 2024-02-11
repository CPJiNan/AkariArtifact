package com.github.cpjinan.plugin.akariartifact.utils

import taboolib.common5.compileJS

object JavaScriptUtil {
    fun String.evalJS(): Any? {
        return eval(this)
    }

    private fun eval(script: String): Any? {
        return script.compileJS()?.eval()?.toString()
    }
}