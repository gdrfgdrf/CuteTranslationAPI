package io.github.gdrfgdrf.cutetranslationapi.extension

import io.github.gdrfgdrf.cutetranslationapi.CuteTranslationAPI

fun String.logInfo() {
    CuteTranslationAPI.log.info(CuteTranslationAPI.LOG_PREFIX + this)
}

fun String.logError(throws: Throwable) {
    CuteTranslationAPI.log.error(CuteTranslationAPI.LOG_PREFIX + this, throws)
}