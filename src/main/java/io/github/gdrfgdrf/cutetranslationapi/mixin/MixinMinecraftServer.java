package io.github.gdrfgdrf.cutetranslationapi.mixin;

import io.github.gdrfgdrf.cutetranslationapi.CuteTranslationAPI;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author gdrfgdrf
 */
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setupServer()Z"), method = "run")
    public void onServerStarting(CallbackInfo ci) {
        CuteTranslationAPI.INSTANCE.onServerStarting();
    }

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void onServerStopping(CallbackInfo info) {
        CuteTranslationAPI.INSTANCE.onServerStopping();
    }

}
