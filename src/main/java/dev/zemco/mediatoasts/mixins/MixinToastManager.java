package dev.zemco.mediatoasts.mixins;

import dev.zemco.mediatoasts.MediaInfoToast;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToastManager.class)
public class MixinToastManager {

    @Unique
    private boolean shouldSuppressSound = false;

    @Unique
    private MutableBoolean soundPlayedFlag;

    @Inject(
        method = "method_61991",
        at = @At(
            value = "RETURN",
            target = "Lnet/minecraft/client/toast/ToastManager$Entry;update()V"
        )
    )
    private <T extends Toast> void mediaToasts$checkIfVisibilityChangeSoundShouldBeSuppressed(
        MutableBoolean soundPlayedFlag,
        ToastManager.Entry<T> entry,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (entry.getInstance() instanceof MediaInfoToast) {
            this.shouldSuppressSound = true;
            this.soundPlayedFlag = soundPlayedFlag;
        }
    }

    @Redirect(
        method = "method_61991",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/toast/Toast$Visibility;playSound(Lnet/minecraft/client/sound/SoundManager;)V"
        )
    )
    private void mediaToasts$suppressVisibilityChangeSound(Toast.Visibility visibility, SoundManager soundManager) {
        if (this.shouldSuppressSound) {
            // don't play toast "whoosh" sound for media info toast
            this.soundPlayedFlag.setFalse();
            return;
        }

        visibility.playSound(soundManager);
    }

}
