package io.github.gdrfgdrf.cutetranslationapi.pool

import io.github.gdrfgdrf.cutetranslationapi.common.GamePlayer
import java.util.concurrent.ConcurrentHashMap

object GamePlayerPool {
    private val pool: ConcurrentHashMap<String, GamePlayer> = ConcurrentHashMap()

    fun getPlayer(playerName: String): GamePlayer? {
        return pool[playerName]
    }

    fun addPlayer(gamePlayer: GamePlayer) {
        pool[gamePlayer.playerName] = gamePlayer
    }

    fun removePlayer(playerName: String) {
        pool.remove(playerName)
    }
}