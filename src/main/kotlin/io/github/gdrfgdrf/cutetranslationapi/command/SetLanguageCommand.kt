package io.github.gdrfgdrf.cutetranslationapi.command

import com.mojang.brigadier.arguments.StringArgumentType
import io.github.gdrfgdrf.cutetranslationapi.command.suggest.LanguageSuggestProvider
import io.github.gdrfgdrf.cutetranslationapi.command.suggest.ModIdSuggestProvider
import io.github.gdrfgdrf.cutetranslationapi.common.Language
import io.github.gdrfgdrf.cutetranslationapi.extension.*
import io.github.gdrfgdrf.cutetranslationapi.manager.ModManager
import io.github.gdrfgdrf.cutetranslationapi.manager.PlayerManager
import io.github.gdrfgdrf.cutetranslationapi.pool.GamePlayerPool
import io.github.gdrfgdrf.cutetranslationapi.utils.command.CommandInvoker
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object SetLanguageCommand : AbstractCommand(
    command = "set-language",
    onlyPlayer = true,
    needOp = false,
    tree = {
        CommandManager.argument("mod-id", StringArgumentType.string())
            .suggests(ModIdSuggestProvider)
            .then(CommandManager.argument("language", StringArgumentType.string())
                .suggests(LanguageSuggestProvider)
                .executes {
                    playerCheck(SetLanguageCommand, it) {
                        argumentCheck(it,
                            argumentCheckers = {
                                notBlank("mod-id".getCommandArgument(it))
                                notBlank("language".getCommandArgument(it))
                            }
                        ) {
                            SetLanguageCommand.set(
                                it.source,
                                "mod-id".getCommandArgument(it),
                                "language".getCommandArgument(it)
                            )
                        }
                    }
                    0
                })
    }
) {
    private fun set(source: ServerCommandSource, modId: String, languageName: String) {
        val commandInvoker = CommandInvoker.of(source)

        val playerName = source.player.name.string
        if (!notBlank(playerName)) {
            return
        }

        val gamePlayer = GamePlayerPool.getPlayer(playerName)
        if (gamePlayer == null) {
            "not_found_game_player".toCommandMessage(playerName)
                .format(playerName)
                .send(commandInvoker)
            return
        }

        val modDescription = ModManager.getMod(modId)
        if (modDescription == null) {
            "not_found_mod".toCommandMessage(playerName)
                .format(modId)
                .send(commandInvoker)
            return
        }

        if (!modDescription.availableLanguages.contains(languageName)) {
            "not_found_language".toCommandMessage(playerName)
                .format(modDescription.displayName, languageName)
                .send(commandInvoker)
            return
        }

        var language: Language? = null
        runCatching {
            language = modDescription.languageHolder.getOrLoad(languageName)
        }.onFailure {
            "An error occurred loading mod $modId's language $languageName".logError(it)
            "load_language_failure".toCommandMessage(playerName)
                .format(languageName, modId)
                .send(commandInvoker)
            return
        }

        val currentLanguage = gamePlayer.getLanguage(modDescription)
        if (currentLanguage != language) {
            gamePlayer.setLanguage(modDescription, language!!)
            PlayerManager.setLanguage(playerName, modDescription, language!!)
        }

        "set_language_success".toCommandMessage(playerName)
            .format(modDescription.displayName, languageName)
            .send(commandInvoker)
    }
}