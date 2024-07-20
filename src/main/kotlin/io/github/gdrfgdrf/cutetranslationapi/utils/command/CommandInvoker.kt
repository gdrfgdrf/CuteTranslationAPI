package io.github.gdrfgdrf.cutetranslationapi.utils.command

import io.github.gdrfgdrf.cutetranslationapi.extension.logInfo
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class CommandInvoker private constructor(private val source: ServerCommandSource) {
    fun sendMessage(text: Text) {
        if (!isConsole()) {
            source.entity?.sendMessage(text)
            return
        }
        text.string.logInfo()
    }

    fun isOp(): Boolean = isConsole() || source.hasPermissionLevel(3)

    fun isConsole(): Boolean = source.entity == null

    companion object {
        fun of(source: ServerCommandSource): CommandInvoker = CommandInvoker(source)
    }
}