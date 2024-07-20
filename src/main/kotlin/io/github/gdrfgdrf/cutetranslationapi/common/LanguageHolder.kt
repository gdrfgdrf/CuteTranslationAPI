package io.github.gdrfgdrf.cutetranslationapi.common

import java.util.concurrent.ConcurrentHashMap

class LanguageHolder private constructor(
    val modDescription: ModDescription
) {
    val map: ConcurrentHashMap<String, Language> = ConcurrentHashMap()
    var defaultLanguage: Language? = null

    fun load(name: String): Language {
        val language = Language.create(modDescription, name)
        map[name] = language
        return language
    }

    fun getOrLoad(name: String): Language {
        if (map.containsKey(name)) {
            return map[name]!!
        }
        return load(name)
    }

    companion object {
        fun create(modDescription: ModDescription): LanguageHolder = LanguageHolder(modDescription)
    }

}