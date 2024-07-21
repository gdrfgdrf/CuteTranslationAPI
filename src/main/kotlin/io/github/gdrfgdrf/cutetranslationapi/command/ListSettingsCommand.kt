package io.github.gdrfgdrf.cutetranslationapi.command

import io.github.gdrfgdrf.cutetranslationapi.extension.notBlank
import io.github.gdrfgdrf.cutetranslationapi.extension.send
import io.github.gdrfgdrf.cutetranslationapi.extension.toCommandMessage
import io.github.gdrfgdrf.cutetranslationapi.manager.PlayerManager
import io.github.gdrfgdrf.cutetranslationapi.utils.command.CommandInvoker
import net.minecraft.server.command.ServerCommandSource

object ListSettingsCommand : AbstractCommand(
    command = "list-settings",
    onlyPlayer = true,
    needOp = false,
    noArgument = true,
    tree = { literalArgumentBuilder ->
        literalArgumentBuilder.executes { context ->
            playerCheck(ListSettingsCommand, context) {
                ListSettingsCommand.list(context.source)
            }
            0
        }
    }
) {
    private fun list(source: ServerCommandSource) {
        val commandInvoker = CommandInvoker.of(source)

        val playerName = source.player?.name?.string!!
        if (!notBlank(playerName)) {
            return
        }

        val settings = PlayerManager.listSettingsWithDefault(playerName)
        if (settings.isEmpty()) {
            "not_found_settings".toCommandMessage(playerName)
                .send(commandInvoker)
            return
        }

        "top".toCommandMessage(playerName)
            .send(commandInvoker)

        "list_settings".toCommandMessage(playerName)
            .send(commandInvoker)

        val itemMessage = "list_settings_item".toCommandMessage(playerName)
        settings.forEach { (modDescription, language) ->
            itemMessage
                .format(
                    language.name,
                    modDescription.modContainer.metadata.id,
                    modDescription.displayName
                )
                .send(commandInvoker)
        }

        "bottom".toCommandMessage(playerName)
            .send(commandInvoker)
    }


}