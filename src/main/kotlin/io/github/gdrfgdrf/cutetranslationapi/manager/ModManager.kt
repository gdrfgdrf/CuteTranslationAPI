package io.github.gdrfgdrf.cutetranslationapi.manager

import io.github.gdrfgdrf.cutetranslationapi.common.ModDescription

object ModManager {
    private val mods: HashMap<String, ModDescription> = HashMap()

    fun addMod(modDescription: ModDescription) {
        mods[modDescription.modContainer.metadata.id] = modDescription
    }

    fun getMod(modId: String): ModDescription? {
        return mods[modId]
    }

    fun hasMod(modId: String): Boolean {
        return mods.containsKey(modId)
    }

    fun allMods(): Map<String, ModDescription> {
        return mods
    }

}