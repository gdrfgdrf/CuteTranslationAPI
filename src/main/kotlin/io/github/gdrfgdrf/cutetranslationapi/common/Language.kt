package io.github.gdrfgdrf.cutetranslationapi.common

import io.github.gdrfgdrf.cutetranslationapi.extension.logInfo
import io.github.gdrfgdrf.cutetranslationapi.extension.notBlank
import io.github.gdrfgdrf.cutetranslationapi.utils.jackson.JacksonUtils
import java.nio.file.Path

class Language private constructor(
    private val modDescription: ModDescription,
    val name: String,
    val file: Path
) {
    private val map: HashMap<String, String> = HashMap()

    fun get(key: String): String? {
        if (!notBlank(key)) {
            throw IllegalArgumentException("The language key cannot be null")
        }
        return map[key]
    }

    fun getOrElse(key: String, otherwise: String): String? {
        if (!notBlank(key)) {
            throw IllegalArgumentException("The language key cannot be null")
        }
        val value = map[key]
        if (!notBlank(value)) {
            return otherwise
        }
        return value
    }

    fun has(key: String): Boolean {
        return map.containsKey(key)
    }

    companion object {
        fun create(
            modDescription: ModDescription,
            name: String
        ): Language {
            val modContainer = modDescription.modContainer
            val modId = modContainer.metadata.id

            val path = modContainer.findPath(Constants.LANGUAGE_FILE_BASE_PATH.format(modId) + name + ".json")
            if (!path.isPresent) {
                throw IllegalStateException("Unable to load $modId's language file $name, because it cannot be found in $modId's assets folder")
            }

            val languageFilePath = path.get()
            val inputStream = languageFilePath.toUri().toURL().openStream()
            val languageNodes = JacksonUtils.readInputStreamTree(inputStream)
            val language = Language(modDescription, name, languageFilePath)

            "Loading language $name for $modId".logInfo()

            languageNodes.keySet().forEach { key ->
                var value = languageNodes.getStringOrNull(key)
                if (!notBlank(value)) {
                    value = ""
                }

                language.map[key] = value
            }

            return language
        }
    }

}