package io.github.gdrfgdrf.cutetranslationapi.common

import io.github.gdrfgdrf.cutetranslationapi.manager.PlayerManager
import java.util.concurrent.ConcurrentHashMap

class GamePlayer private constructor(
    val playerName: String
) {
     private val map: ConcurrentHashMap<ModDescription, Language> = ConcurrentHashMap()

    fun initialize() {
        val settings = PlayerManager.listSettings(playerName)
        if (settings.isNotEmpty()) {
            map.putAll(settings)
        }
    }

    fun setLanguage(modDescription: ModDescription, language: Language) {
        if (modDescription.languageHolder.defaultLanguage == language) {
            map.remove(modDescription)
            return
        }
        map[modDescription] = language
    }

    fun getLanguage(modDescription: ModDescription): Language? {
        if (!map.containsKey(modDescription)) {
            return modDescription.languageHolder.defaultLanguage
        }
        return map[modDescription]
    }

    companion object {
        fun create(playerName: String): GamePlayer = GamePlayer(playerName)
    }
}