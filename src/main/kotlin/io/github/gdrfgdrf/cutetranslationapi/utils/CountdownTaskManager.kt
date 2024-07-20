/*
 * Copyright 2024 CuteTrade's contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.gdrfgdrf.cutetranslationapi.utils

import io.github.gdrfgdrf.cutetranslationapi.extension.logError
import io.github.gdrfgdrf.cutetranslationapi.extension.logInfo
import io.github.gdrfgdrf.cutetranslationapi.extension.runCoroutineTask
import io.github.gdrfgdrf.cutetranslationapi.extension.sleepSafety
import io.github.gdrfgdrf.cutetranslationapi.utils.thread.ThreadPoolService
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

object CountdownTaskManager {
    private var stop = false
    private val tasks = CopyOnWriteArrayList<CountdownTask>()

    fun start() {
        "Starting countdown task manager".logInfo()

        stop = false
        ThreadPoolService.newTask(Worker)
    }

    fun terminate() {
        stop = true
        tasks.clear()

        "Countdown task manager terminated".logInfo()
    }

    fun add(task: CountdownTask) {
        task.startTime = System.currentTimeMillis()
        tasks.add(task)
    }

    object Worker : Runnable {
        override fun run() {
            "Countdown task worker started".logInfo()

            while (!stop) {
                runCatching {
                    val now = System.currentTimeMillis()

                    tasks.forEach { task ->
                        if (task.end) {
                            tasks.remove(task)
                            return@forEach
                        }

                        val timeout = TimeUnit.MILLISECONDS.convert(task.timeout, task.timeUnit)

                        if (now - task.startTime >= timeout) {
                            tasks.remove(task)
                            runCoroutineTask {
                                task.endRun()
                            }
                        }
                    }

                }.onFailure {
                    "Error on countdown worker".logError(it)
                }

                sleepSafety(100)
            }
            stop = false

            "Countdown task worker terminated".logInfo()
        }
    }

    class CountdownTask(
        val timeout: Long,
        val timeUnit: TimeUnit,
        val endRun: () -> Unit
    ) {
        var startTime: Long = 0L
        var end: Boolean = false

        fun copy(): CountdownTask {
            return create(timeout, timeUnit, endRun)
        }

        companion object {
            fun create(
                timeout: Long,
                timeUnit: TimeUnit,
                endRun: () -> Unit
            ) = CountdownTask(timeout, timeUnit, endRun)
        }
    }

}