package io.github.gdrfgdrf.cutetranslationapi.provider

import io.github.gdrfgdrf.cutetranslationapi.common.ModDescription
import io.github.gdrfgdrf.cutetranslationapi.external.ExternalTranslationProvider
import io.github.gdrfgdrf.cutetranslationapi.manager.ModManager
import java.util.concurrent.ConcurrentHashMap

object TranslationProviderManager {
    private val map: ConcurrentHashMap<ModDescription, ExternalTranslationProvider> = ConcurrentHashMap()

    /**
     * Get the language provider
     */
    fun getOrCreate(modId: String): ExternalTranslationProvider {
        val modDescription = ModManager.getMod(modId) ?: throw IllegalArgumentException("Unknown mod id $modId")
        if (map.containsKey(modDescription)) {
            return map[modDescription]!!
        }

        val defaultLanguage = modDescription.languageHolder.defaultLanguage
            ?: throw IllegalStateException("Mod $modId's default language is not loaded normally")

        val provider = object : ExternalTranslationProvider() {
            override fun get(key: String): String? {
                return defaultLanguage.get(key)
            }

            override fun getOrElse(key: String, otherwise: String): String? {
                return defaultLanguage.getOrElse(key, otherwise)
            }

            override fun has(key: String): Boolean {
                return defaultLanguage.has(key)
            }
        }
        map[modDescription] = provider

        return provider
    }

}