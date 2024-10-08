package com.github.cpjinan.plugin.akariartifact.core.common.script.javascript

import taboolib.common5.compileJS
import taboolib.common5.util.replace

object JavaScript {
    fun String.evalJS(
        args: Map<String, Any>? = null
    ): Any? {
        return eval(this, args)
    }

    private fun eval(
        script: String,
        args: Map<String, Any>? = null,
    ): Any? {
        args?.forEach { (k, v) -> script.replace(Pair(k, v)) }
        return script.compileJS()?.eval()
    }
}