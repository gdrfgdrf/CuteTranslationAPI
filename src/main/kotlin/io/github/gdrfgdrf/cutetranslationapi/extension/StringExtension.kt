package io.github.gdrfgdrf.cutetranslationapi.extension

import com.mojang.brigadier.context.CommandContext
import io.github.gdrfgdrf.cutetranslationapi.CuteTranslationAPI
import io.github.gdrfgdrf.cutetranslationapi.utils.command.CommandInvoker
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText

internal fun String.toCommandMessage(): String {
    return CuteTranslationAPI.externalTranslationProvider?.get("command.cutetranslationapi.$this") ?: ""
}

internal fun String.toCommandMessage(playerName: String): String {
    return CuteTranslationAPI.externalPlayerTranslationProvider?.get(playerName, "command.cutetranslationapi.$this") ?: ""
}

internal fun String.send(commandInvoker: CommandInvoker) {
    commandInvoker.sendMessage(LiteralText(this.replace("&", "§")))
}

internal fun String.getCommandArgument(context: CommandContext<ServerCommandSource>): String {
    val result = context.getArgumentSafety(this, String::class.java)
    return if (notBlank(result)) {
        result!!
    } else {
        ""
    }
}

internal fun length(string: String?): Int {
    if (string == null) {
        return 0
    }
    return string.length
}

fun notBlank(string: String?): Boolean {
    val length = length(string)
    if (length == 0) {
        return false
    }
    for (i in 0 until length) {
        if (!Character.isWhitespace(string!![i])) {
            return true
        }
    }
    return false
}