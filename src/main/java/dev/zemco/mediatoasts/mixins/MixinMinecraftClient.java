package dev.zemco.mediatoasts.mixins;

import dev.zemco.mediatoasts.MediaToastsClientMod;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/font/FontManager;close()V"
        ),
        method = "close"
    )
    public void preClose(CallbackInfo ci) {
        MediaToastsClientMod.getInstance().onCloseClient();
    }

}
