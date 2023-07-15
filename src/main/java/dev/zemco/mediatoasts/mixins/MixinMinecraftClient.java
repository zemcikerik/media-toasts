package dev.zemco.mediatoasts.mixins;

import dev.zemco.mediatoasts.MediaToastsClientMod;
import dev.zemco.mediatoasts.PlatformChecker;
import dev.zemco.mediatoasts.UnsupportedPlatformScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Unique
    private boolean mediaToasts$warningDisplayed = false;

    @Shadow
    public abstract void setScreen(Screen screen);

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    private void mediaToasts$preSetScreen(Screen screen, CallbackInfo ci) {
        PlatformChecker platformChecker = MediaToastsClientMod.getInstance().getPlatformChecker();

        boolean shouldDisplayWarning = !this.mediaToasts$warningDisplayed
            && screen instanceof TitleScreen
            && !platformChecker.isSupportedPlatform();

        if (shouldDisplayWarning) {
            this.mediaToasts$warningDisplayed = true;
            this.setScreen(new UnsupportedPlatformScreen(screen));
            ci.cancel();
        }
    }

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/font/FontManager;close()V"
        ),
        method = "close"
    )
    private void mediaToasts$preClose(CallbackInfo ci) {
        MediaToastsClientMod.getInstance().closeMediaHandler();
    }

}
