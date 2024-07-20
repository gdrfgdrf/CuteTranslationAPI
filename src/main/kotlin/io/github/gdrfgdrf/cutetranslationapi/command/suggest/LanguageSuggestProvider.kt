package io.github.gdrfgdrf.cutetranslationapi.command.suggest

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.gdrfgdrf.cutetranslationapi.extension.getCommandArgument
import io.github.gdrfgdrf.cutetranslationapi.extension.notBlank
import io.github.gdrfgdrf.cutetranslationapi.manager.ModManager
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

object LanguageSuggestProvider : SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val providedModId = "mod-id".getCommandArgument(context)
        if (!notBlank(providedModId)) {
            return builder.buildFuture()
        }

        val modDescription = ModManager.getMod(providedModId) ?: return builder.buildFuture()
        val availableLanguages = modDescription.availableLanguages
        availableLanguages.forEach(builder::suggest)

        return builder.buildFuture()
    }
}