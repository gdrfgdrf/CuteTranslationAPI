package io.github.gdrfgdrf.cutetranslationapi.common

import io.github.gdrfgdrf.cutetranslationapi.external.ExternalModDescription
import net.fabricmc.loader.api.ModContainer

class ModDescription private constructor(
    val modContainer: ModContainer,
    var displayName: String
) {
    val languageHolder: LanguageHolder = LanguageHolder.create(this)
    val availableLanguages: ArrayList<String> = ArrayList()

    companion object {
        fun create(modContainer: ModContainer, externalModDescription: ExternalModDescription): ModDescription {
            val modDescription = ModDescription(modContainer, externalModDescription.displayName)

            val availableLanguages = externalModDescription.languages
            availableLanguages.forEach {
                modDescription.availableLanguages.add(it.name)
            }

            val defaultLanguage = modDescription.languageHolder.load(externalModDescription.defaultLanguage)
            modDescription.languageHolder.defaultLanguage = defaultLanguage

            return modDescription
        }
    }

}