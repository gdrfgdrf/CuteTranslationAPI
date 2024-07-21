package io.github.gdrfgdrf.cutetranslationapi.command.admin

import io.github.gdrfgdrf.cutetranslationapi.command.AbstractCommand
import io.github.gdrfgdrf.cutetranslationapi.extension.logError
import io.github.gdrfgdrf.cutetranslationapi.extension.send
import io.github.gdrfgdrf.cutetranslationapi.extension.toCommandMessage
import io.github.gdrfgdrf.cutetranslationapi.manager.PlayerManager
import io.github.gdrfgdrf.cutetranslationapi.utils.command.CommandInvoker
import net.minecraft.server.command.ServerCommandSource

object SaveDataAdminCommand : AbstractCommand(
    command = "save-data",
    onlyPlayer = false,
    needOp = true,
    noArgument = true,
    tree = { literalArgumentBuilder ->
        literalArgumentBuilder.executes {
            playerCheck(SaveDataAdminCommand, it) {
                SaveDataAdminCommand.save(it.source)
            }
            0
        }
    }
) {
    private fun save(source: ServerCommandSource) {
        val commandInvoker = CommandInvoker.of(source)

        runCatching {
            PlayerManager.save()

            if (commandInvoker.isConsole()) {
                "save_data_success".toCommandMessage()
                    .send(commandInvoker)
            } else {
                "save_data_success".toCommandMessage(source.player?.name?.string!!)
                    .send(commandInvoker)
            }
        }.onFailure {
            "An error occurred while saving player language data".logError(it)

            if (commandInvoker.isConsole()) {
                "save_data_failure".toCommandMessage()
                    .send(commandInvoker)
            } else {
                "save_data_failure".toCommandMessage(source.player?.name?.string!!)
                    .send(commandInvoker)
            }
        }

    }

}