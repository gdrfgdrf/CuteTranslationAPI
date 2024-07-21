package io.github.gdrfgdrf.cutetranslationapi.mixin;

import io.github.gdrfgdrf.cutetranslationapi.CuteTranslationAPI;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author gdrfgdrf
 */
@Mixin(CommandManager.class)
public abstract class MixinCommandManager {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;findAmbiguities(Lcom/mojang/brigadier/AmbiguityConsumer;)V"), method = "<init>")
    private void registerCommands(boolean isDedicatedServer, CallbackInfo ci) {
        if (!isDedicatedServer) {
            CuteTranslationAPI.INSTANCE.registerCommand(((CommandManager) (Object) this).getDispatcher());
        }
    }

}
