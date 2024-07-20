package io.github.gdrfgdrf.cutetranslationapi.utils.task.worker

import io.github.gdrfgdrf.cutetranslationapi.utils.task.entry.FutureTaskEntry
import io.github.gdrfgdrf.cutetranslationapi.utils.task.TaskManager
import io.github.gdrfgdrf.cutetranslationapi.extension.logInfo
import io.github.gdrfgdrf.cutetranslationapi.extension.runCoroutineTask
import io.github.gdrfgdrf.cutetranslationapi.extension.sleepSafety
import java.util.concurrent.LinkedBlockingQueue

object TaskWorker : Runnable {
    override fun run() {
        "Task worker started".logInfo()

        while (!TaskManager.isTerminated()) {
            val taskEntry = TaskManager.TASK_ENTRY_QUEUE.poll() ?: continue

            if (taskEntry.customLock == null) {
                runCoroutineTask {
                    val result = taskEntry.supplier()

                    if (taskEntry is FutureTaskEntry) {
                        taskEntry.result(result)
                    } else {
                        taskEntry.notifyMethodFinished()
                    }
                }
            } else {
                val taskEntries = TaskManager.SYNCHRONIZED_TASK_ENTRY.computeIfAbsent(taskEntry.customLock!!) {
                    LinkedBlockingQueue()
                }

                taskEntries.put(taskEntry)
            }

            sleepSafety(100)
        }

        "Task worker terminated".logInfo()
    }
}