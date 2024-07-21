package io.github.gdrfgdrf.cutetranslationapi.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import io.github.gdrfgdrf.cutetranslationapi.CuteTranslationAPI;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.UserCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.net.Proxy;

/**
 * @author gdrfgdrf
 */
@Mixin(MinecraftDedicatedServer.class)
public abstract class MixinMinecraftDedicateServer extends MinecraftServer {
    public MixinMinecraftDedicateServer(
            File gameDir,
            Proxy proxy,
            DataFixer dataFixer,
            CommandManager commandManager,
            YggdrasilAuthenticationService authService,
            MinecraftSessionService sessionService,
            GameProfileRepository gameProfileRepository,
            UserCache userCache,
            WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory,
            String levelName
    ) {
        super(gameDir,
                proxy,
                dataFixer,
                commandManager,
                authService,
                sessionService,
                gameProfileRepository,
                userCache,
                worldGenerationProgressListenerFactory,
                levelName);
    }

    @Inject(method = "setupServer", at = @At("HEAD"))
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
        CuteTranslationAPI.INSTANCE.registerCommand(getCommandManager().getDispatcher());
    }
}
