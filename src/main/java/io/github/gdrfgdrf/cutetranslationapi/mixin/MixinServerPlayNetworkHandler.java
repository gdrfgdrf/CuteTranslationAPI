package io.github.gdrfgdrf.cutetranslationapi.mixin;

import io.github.gdrfgdrf.cutetranslationapi.CuteTranslationAPI;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author gdrfgdrf
 */
@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {

    @Inject(method = "disconnect(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    public void disconnect(Text reason, CallbackInfo ci) {
        CuteTranslationAPI.INSTANCE.playerDisconnect((ServerPlayNetworkHandler) (Object) this);
    }
}
