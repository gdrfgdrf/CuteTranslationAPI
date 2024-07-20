package io.github.gdrfgdrf.cutetranslationapi.extension

import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

fun <T> CommandContext<ServerCommandSource>.getArgumentSafety(name: String, type: Class<out T>): T? {
    var argument: T? = null

    runCatching {
        argument = this.getArgument(name, type)
    }.onFailure {
        argument = null
    }

    return argument
}