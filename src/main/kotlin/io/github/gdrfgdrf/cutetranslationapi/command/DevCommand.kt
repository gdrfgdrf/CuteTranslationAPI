package io.github.gdrfgdrf.cutetranslationapi.command

import io.github.gdrfgdrf.cutetranslationapi.text.CuteTranslation
import net.minecraft.server.command.ServerCommandSource

object DevCommand : AbstractCommand(
    command = "dev",
    onlyPlayer = true,
    needOp = false,
    noArgument = true,
    tree = { literalArgumentBuilder ->
        literalArgumentBuilder.executes {
            playerCheck(DevCommand, it) {
                DevCommand.dev(it.source)
            }
            0
        }
    }
) {

    private fun dev(source: ServerCommandSource) {
        val cuteTranslation = CuteTranslation.build("&a&ltest1", " &c&ltest2", " &e&l你好世界")
        cuteTranslation.get(0)
            .runCommand("/w ${source.player.name.string} \"Hello World test1\"")
            .showText("test1")
        cuteTranslation.get(1)
            .runCommand("/w ${source.player.name.string} \"Hello World test2\"")
            .showText("test2")
        cuteTranslation.get(2)
            .runCommand("/w ${source.player.name.string} \"Hello World test3\"")
            .showText("test3")
        cuteTranslation.send(source.player)
    }

}