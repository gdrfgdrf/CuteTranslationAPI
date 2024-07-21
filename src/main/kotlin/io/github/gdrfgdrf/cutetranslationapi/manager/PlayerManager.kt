package io.github.gdrfgdrf.cutetranslationapi.manager

import cutetranslationapi.protobuf.CommonProto.Player
import cutetranslationapi.protobuf.StorableProto.Store
import io.github.gdrfgdrf.cutetranslationapi.common.Language
import io.github.gdrfgdrf.cutetranslationapi.common.ModDescription
import io.github.gdrfgdrf.cutetranslationapi.extension.logError
import io.github.gdrfgdrf.cutetranslationapi.extension.logInfo
import io.github.gdrfgdrf.cutetranslationapi.extension.runSyncTask
import io.github.gdrfgdrf.cutetranslationapi.utils.CountdownTaskManager
import io.github.gdrfgdrf.cutetranslationapi.utils.Protobuf
import java.util.concurrent.TimeUnit

object PlayerManager {
    var store: Protobuf<Store>? = null
    private var countdownTask = CountdownTaskManager.CountdownTask.create(5, TimeUnit.MINUTES) {
        runCatching {
            save()
        }.onFailure {
            "An error occurred while saving player language data".logError(it)
        }
    }

    fun startSaveTask() {
        CountdownTaskManager.add(countdownTask)
    }

    private fun restartSaveTask() {
        countdownTask.end = true
        val copied = countdownTask.copy()
        countdownTask = copied
        CountdownTaskManager.add(copied)
    }

    fun save() {
        check()
        store!!.save()
        "Player language data is saved".logInfo()

        restartSaveTask()
    }

    fun listSettingsWithDefault(playerName: String): Map<ModDescription, Language> {
        check()
        val player = getPlayer(playerName)
        val resultMap = HashMap<ModDescription, Language>()

        val map = player.modIdToNeedLanguageMap
        if (map.isEmpty()) {
            ModManager.allMods().forEach { (_, modDescription) ->
                val defaultLanguage = modDescription.languageHolder.defaultLanguage ?: return@forEach
                resultMap[modDescription] = defaultLanguage
            }
            return resultMap
        }

        map.forEach { (modId, languageName) ->
            val modDescription = ModManager.getMod(modId) ?: return@forEach
            val language = modDescription.languageHolder.map[languageName] ?: return@forEach

            resultMap[modDescription] = language
        }

        ModManager.allMods().forEach { (_, modDescription) ->
            if (resultMap.containsKey(modDescription)) {
                return@forEach
            }
            val defaultLanguage = modDescription.languageHolder.defaultLanguage ?: return@forEach
            resultMap[modDescription] = defaultLanguage
        }

        return resultMap
    }

    fun listSettings(playerName: String): Map<ModDescription, Language> {
        check()
        val player = getPlayer(playerName)
        val resultMap = HashMap<ModDescription, Language>()

        val map = player.modIdToNeedLanguageMap
        if (map.isEmpty()) {
            return resultMap
        }

        map.forEach { (modId, languageName) ->
            val modDescription = ModManager.getMod(modId) ?: return@forEach
            val language = modDescription.languageHolder.getOrLoad(languageName)

            resultMap[modDescription] = language
        }

        return resultMap
    }

    fun setLanguage(
        playerName: String,
        modDescription: ModDescription,
        language: Language,
    ) {
        check()
        runSyncTask(store!!) {
            val defaultLanguageName = modDescription.languageHolder.defaultLanguage?.name
            val modId = modDescription.modContainer.metadata.id
            val languageName = language.name

            var player: Player = getPlayer(playerName)
            player = if (languageName != defaultLanguageName) {
                player.toBuilder()
                    .putModIdToNeedLanguage(modId, languageName)
                    .build()
            } else {
                player.toBuilder()
                    .removeModIdToNeedLanguage(modId)
                    .build()
            }

            var newStore: Store = store!!.message!!
            newStore = if (player.modIdToNeedLanguageCount == 0) {
                newStore.toBuilder()
                    ?.removePlayerNameToPlayer(playerName)
                    ?.build()!!
            } else {
                newStore.toBuilder()
                    ?.putPlayerNameToPlayer(playerName, player)
                    ?.build()!!
            }

            store!!.message = newStore
        }
    }

    private fun getPlayer(playerName: String): Player {
        check()
        return store!!.message?.getPlayerNameToPlayerOrDefault(playerName, Player.newBuilder().build())!!
    }

    private fun check() {
        if (store == null) {
            throw IllegalStateException("CuteTranslationAPI's data store is not initialized properly")
        }
    }
}