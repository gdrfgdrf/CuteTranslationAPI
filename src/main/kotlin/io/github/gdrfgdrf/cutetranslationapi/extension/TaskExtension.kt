package io.github.gdrfgdrf.cutetranslationapi.extension

import io.github.gdrfgdrf.cutetranslationapi.utils.task.entry.FutureTaskEntry
import io.github.gdrfgdrf.cutetranslationapi.utils.task.entry.TaskEntry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

@OptIn(DelicateCoroutinesApi::class)
fun runCoroutineTask(runnable: () -> Unit) {
    GlobalScope.launchIO {
        runnable()
    }
}

fun runAsyncTask(runnable: () -> Unit) {
    TaskEntry<Any>(runnable)
        .run()
}

fun <T> runSyncTask(lock: Any, supplier: () -> T?) {
    FutureTaskEntry.create(supplier)
        .customLock(lock)
        .run()
}

