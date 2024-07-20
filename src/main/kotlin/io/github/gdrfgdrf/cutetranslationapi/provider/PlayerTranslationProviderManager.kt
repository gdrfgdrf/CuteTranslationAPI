package io.github.gdrfgdrf.cutetranslationapi.provider

import io.github.gdrfgdrf.cutetranslationapi.common.ModDescription
import io.github.gdrfgdrf.cutetranslationapi.external.ExternalPlayerTranslationProvider
import io.github.gdrfgdrf.cutetranslationapi.manager.ModManager
import io.github.gdrfgdrf.cutetranslationapi.pool.GamePlayerPool
import java.util.concurrent.ConcurrentHashMap

object PlayerTranslationProviderManager {
    private val map: ConcurrentHashMap<ModDescription, ExternalPlayerTranslationProvider> = ConcurrentHashMap()

    /**
     * Gets a provider that provides a string based on player settings
     */
    fun getOrCreate(modId: String): ExternalPlayerTranslationProvider {
        val modDescription = ModManager.getMod(modId) ?: throw IllegalArgumentException("Unknown mod id $modId")
        if (map.containsKey(modDescription)) {
            return map[modDescription]!!
        }

        val provider = object : ExternalPlayerTranslationProvider() {
            override fun get(playerName: String, key: String): String? {
                val player = GamePlayerPool.getPlayer(playerName) ?: return null
                val language = player.getLanguage(modDescription) ?: return null
                return language.get(key)
            }

            override fun getOrElse(playerName: String, key: String, otherwise: String): String? {
                val player = GamePlayerPool.getPlayer(playerName) ?: return null
                val language = player.getLanguage(modDescription) ?: return null
                return language.getOrElse(key, otherwise)
            }

            override fun has(playerName: String, key: String): Boolean {
                val player = GamePlayerPool.getPlayer(playerName) ?: return false
                val language = player.getLanguage(modDescription) ?: return false
                return language.has(key)
            }
        }
        map[modDescription] = provider

        return provider
    }
}