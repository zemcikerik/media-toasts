package dev.zemco.mediatoasts;

import net.minecraft.client.toast.ToastManager;

import static com.google.common.base.Preconditions.checkArgument;

public class ToastMediaInfoListener implements MediaInfoListener {

    private final ToastManager toastManager;

    public ToastMediaInfoListener(ToastManager toastManager) {
        checkArgument(toastManager != null, "Toast manager cannot be null!");
        this.toastManager = toastManager;
    }

    @Override
    public void onMediaInfo(MediaInfo mediaInfo) {
        this.toastManager.add(new MediaInfoToast(mediaInfo));
    }

}
