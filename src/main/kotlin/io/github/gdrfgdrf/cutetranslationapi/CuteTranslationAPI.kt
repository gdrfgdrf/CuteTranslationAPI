package io.github.gdrfgdrf.cutetranslationapi

import com.google.protobuf.Message
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import cutetranslationapi.protobuf.StorableProto.Store
import io.github.gdrfgdrf.cutetranslationapi.command.DevCommand
import io.github.gdrfgdrf.cutetranslationapi.command.ListSettingsCommand
import io.github.gdrfgdrf.cutetranslationapi.command.SetLanguageCommand
import io.github.gdrfgdrf.cutetranslationapi.command.admin.SaveDataAdminCommand
import io.github.gdrfgdrf.cutetranslationapi.common.Constants
import io.github.gdrfgdrf.cutetranslationapi.common.GamePlayer
import io.github.gdrfgdrf.cutetranslationapi.common.ModDescription
import io.github.gdrfgdrf.cutetranslationapi.extension.logInfo
import io.github.gdrfgdrf.cutetranslationapi.extension.runCoroutineTask
import io.github.gdrfgdrf.cutetranslationapi.external.ExternalModDescription
import io.github.gdrfgdrf.cutetranslationapi.external.ExternalPlayerTranslationProvider
import io.github.gdrfgdrf.cutetranslationapi.external.ExternalTranslationProvider
import io.github.gdrfgdrf.cutetranslationapi.manager.ModManager
import io.github.gdrfgdrf.cutetranslationapi.manager.PlayerManager
import io.github.gdrfgdrf.cutetranslationapi.pool.GamePlayerPool
import io.github.gdrfgdrf.cutetranslationapi.provider.PlayerTranslationProviderManager
import io.github.gdrfgdrf.cutetranslationapi.provider.TranslationProviderManager
import io.github.gdrfgdrf.cutetranslationapi.utils.CountdownTaskManager
import io.github.gdrfgdrf.cutetranslationapi.utils.Protobuf
import io.github.gdrfgdrf.cutetranslationapi.utils.jackson.JacksonUtils
import io.github.gdrfgdrf.cutetranslationapi.utils.task.TaskManager
import io.github.gdrfgdrf.cutetranslationapi.utils.thread.ThreadPoolService
import net.fabricmc.api.EnvType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayNetworkHandler
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.nio.file.Path

object CuteTranslationAPI : ModInitializer {
	const val MOD_ID = "cutetranslationapi"
	const val LOG_PREFIX = "[CuteTranslationAPI] >> "
    val log: Logger = LogManager.getLogger("CuteTranslationAPI")

	var TRANSLATION_PROVIDER: ExternalTranslationProvider? = null
	var PLAYER_TRANSLATION_PROVIDER: ExternalPlayerTranslationProvider? = null

	override fun onInitialize() {
		val envType = FabricLoader.getInstance().environmentType
		"Environment $envType".logInfo()

		prepareProtobuf()

		val modContainerOptional = FabricLoader.getInstance().getModContainer(MOD_ID)
		if (!modContainerOptional.isPresent) {
			throw IllegalStateException("Cannot found cute translation api's mod container")
		}
		val cuteTranslationAPIModContainer = modContainerOptional.get()
		load(cuteTranslationAPIModContainer)

		val mods = FabricLoader.getInstance().allMods
		mods.forEach {
			if (it == cuteTranslationAPIModContainer) {
				return@forEach
			}
			load(it)
		}

		PlayerManager.startSaveTask()

		TRANSLATION_PROVIDER = TranslationProviderManager.getOrCreate(MOD_ID)
		PLAYER_TRANSLATION_PROVIDER = PlayerTranslationProviderManager.getOrCreate(MOD_ID)

		prepareEventListener()

		TaskManager.start()
		CountdownTaskManager.start()
	}

	private fun playerJoin(handler: ServerPlayNetworkHandler) {
		runCoroutineTask {
			val name = handler.player.name.string
			val gamePlayer = GamePlayer.create(name)

			gamePlayer.initialize()
			GamePlayerPool.addPlayer(gamePlayer)
		}
	}

	private fun playerDisconnect(handler: ServerPlayNetworkHandler) {
		runCoroutineTask {
			val name = handler.player.name.string
			GamePlayerPool.removePlayer(name)
		}
	}

	private fun onServerStopping() {
		PlayerManager.save()

		TaskManager.terminate()
		ThreadPoolService.terminate()
		CountdownTaskManager.terminate()
	}

	private fun registerCommand(dispatcher: CommandDispatcher<ServerCommandSource>) {
		val allCommands = listOf(
			ListSettingsCommand,
			SetLanguageCommand,
			DevCommand
		)
		val adminCommands = listOf(
			SaveDataAdminCommand
		)

		val common = LiteralArgumentBuilder.literal<ServerCommandSource>("language")
		val admin = LiteralArgumentBuilder.literal<ServerCommandSource>("language-admin")
			.requires {
				it.player?.allowsPermissionLevel(3) == true
			}

		allCommands.forEach { command ->
			"Registering command ${command::class.simpleName}".logInfo()
			command.register(common)
		}
		adminCommands.forEach { command ->
			"Registering admin command ${command::class.simpleName}".logInfo()
			command.register(admin)
		}

		dispatcher.register(
			common
		)
		dispatcher.register(
			admin
		)
	}

	private fun prepareEventListener() {
		ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
			playerJoin(handler)
		}
		ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
			playerDisconnect(handler)
		}
		CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			registerCommand(dispatcher)
		}
		ServerLifecycleEvents.SERVER_STOPPING.register {
			onServerStopping()
		}
	}

	private fun prepareProtobuf() {
		PlayerManager.store = prepareProtobufFile(
			File(Constants.STORE_FILE_PATH),
			Store.newBuilder()::build,
			Store::parseFrom
		)
	}

	private fun load(modContainer: ModContainer) {
		val modId = modContainer.metadata.id

		val path = modContainer.findPath("cutetranslationapi.json")
		if (!path.isPresent) {
			return
		}
		"Loading languages for $modId".logInfo()

		val modDescription = prepareModDescription(modContainer, path.get())
		ModManager.addMod(modDescription)
	}

	private fun prepareModDescription(modContainer: ModContainer, path: Path): ModDescription {
		val modId = modContainer.metadata.id
		val inputStream = path.toUri().toURL().openStream()
		var externalModDescription: ExternalModDescription? = null

		runCatching {
			externalModDescription = JacksonUtils.readInputStream(inputStream, ExternalModDescription::class.java)
			if (externalModDescription == null) {
				throw IllegalStateException("External mod description is null")
			}
		}.onFailure {
			throw RuntimeException("Cannot load the mod description file of $modId")
		}

		return ModDescription.create(modContainer, externalModDescription!!)
	}

	private fun <T : Message> prepareProtobufFile(
		protobufFile: File,
		buildFunction: () -> T,
		parseFunction: (ByteArray) -> T,
	): Protobuf<T> {
		"Preparing protobuf file: $protobufFile".logInfo()

		if (!protobufFile.exists()) {
			protobufFile.createNewFile()
			val protobuf = Protobuf<T>()
			protobuf.message = buildFunction()
			protobuf.storeFile = protobufFile
			protobuf.save()

			"Prepared protobuf file: $protobufFile".logInfo()
			return protobuf
		}
		val protobuf = Protobuf.get(protobufFile, parseFunction)
		"Prepared protobuf file: $protobufFile".logInfo()

		return protobuf!!
	}
}