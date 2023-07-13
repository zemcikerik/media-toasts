package dev.zemco.mediatoasts.mixins;

import dev.zemco.mediatoasts.MediaInfoToast;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.toast.Toast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net/minecraft/client/toast/ToastManager$Entry")
public class MixinToastManagerEntry<T extends Toast> {

    @Final
    @Shadow
    private T instance;

    @Redirect(
        at = @At(
            target = "Lnet/minecraft/client/toast/Toast$Visibility;playSound(Lnet/minecraft/client/sound/SoundManager;)V",
            value = "INVOKE"
        ),
        method = "draw"
    )
    private void mediaToasts$redirectPlayVisibilitySound(Toast.Visibility visibility, SoundManager soundManager) {
        if (this.instance instanceof MediaInfoToast) {
            // don't play toast "whoosh" sound for media info toast
            return;
        }

        visibility.playSound(soundManager);
    }

}
