package io.github.gdrfgdrf.cutetranslationapi.utils.task

import io.github.gdrfgdrf.cutetranslationapi.utils.task.entry.TaskEntry
import io.github.gdrfgdrf.cutetranslationapi.utils.task.worker.SyncTaskWorker
import io.github.gdrfgdrf.cutetranslationapi.utils.task.worker.TaskWorker
import io.github.gdrfgdrf.cutetranslationapi.utils.thread.ThreadPoolService
import io.github.gdrfgdrf.cutetranslationapi.extension.logInfo
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue

object TaskManager {
    val TASK_ENTRY_QUEUE = ConcurrentLinkedQueue<TaskEntry<*>>()
    val SYNCHRONIZED_TASK_ENTRY = ConcurrentHashMap<Any, LinkedBlockingQueue<TaskEntry<*>>>()
    private val TASK_WORKER = TaskWorker
    private val SYNCHRONIZED_TASK_WORKER = SyncTaskWorker

    private var terminated = false

    fun start() {
        "Starting task manager".logInfo()

        terminated = false

        ThreadPoolService.newTask(TASK_WORKER)
        ThreadPoolService.newTask(SYNCHRONIZED_TASK_WORKER)
    }

    fun isTerminated(): Boolean = terminated

    fun terminate() {
        terminated = true
        TASK_ENTRY_QUEUE.clear()
        SYNCHRONIZED_TASK_ENTRY.clear()
        "Task manager terminated".logInfo()
    }

    fun add(taskEntry: TaskEntry<*>) {
        if (terminated) {
            throw IllegalStateException("Task manager has been terminated")
        }
        TASK_ENTRY_QUEUE.offer(taskEntry)
    }




}