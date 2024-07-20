package io.github.gdrfgdrf.cutetranslationapi.utils.task.worker

import io.github.gdrfgdrf.cutetranslationapi.extension.*
import io.github.gdrfgdrf.cutetranslationapi.utils.task.entry.FutureTaskEntry
import io.github.gdrfgdrf.cutetranslationapi.utils.task.TaskManager

object SyncTaskWorker : Runnable {
    override fun run() {
        "Synchronized task worker started".logInfo()

        while (!TaskManager.isTerminated()) {
            TaskManager.SYNCHRONIZED_TASK_ENTRY.forEach { (lock, taskEntries) ->
                TaskManager.SYNCHRONIZED_TASK_ENTRY.remove(lock)

                runCoroutineTask {
                    var nextRound = true

                    while (nextRound) {
                        runCatching {
                            val taskEntry = taskEntries.take()

                            if (lock is String) {
                                synchronized(lock.intern()) {
                                    val result = taskEntry.supplier()

                                    if (taskEntry is FutureTaskEntry) {
                                        taskEntry.result(result)
                                    } else {
                                        taskEntry.notifyMethodFinished()
                                    }
                                }
                            } else {
                                synchronized(lock) {
                                    val result = taskEntry.supplier()

                                    if (taskEntry is FutureTaskEntry) {
                                        taskEntry.result(result)
                                    } else {
                                        taskEntry.notifyMethodFinished()
                                    }
                                }
                            }
                        }.onFailure {
                            "InterruptedException when taking out task from linked blocking queue".logError(it)
                        }

                        if (taskEntries.isEmpty()) {
                            nextRound = false
                        }
                    }
                }
            }

            sleepSafety(100)
        }

        "Synchronized task worker terminated".logInfo()
    }
}