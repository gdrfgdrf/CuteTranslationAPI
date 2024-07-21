package io.github.gdrfgdrf.cutetranslationapi.mixin;

import io.github.gdrfgdrf.cutetranslationapi.CuteTranslationAPI;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author gdrfgdrf
 */
@Mixin(PlayerManager.class)
public abstract class MixinServerPlayNetworkAddon {

    @Inject(method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("RETURN"))
    private void onClientReady(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        CuteTranslationAPI.INSTANCE.playerJoin(player.networkHandler);
    }
}
