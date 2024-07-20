package io.github.gdrfgdrf.cutetranslationapi.extension

fun sleepSafety(millis: Long) {
    runCatching {
        Thread.sleep(millis)
    }.onFailure {

    }
}