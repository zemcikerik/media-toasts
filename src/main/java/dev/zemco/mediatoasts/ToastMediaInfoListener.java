package dev.zemco.mediatoasts;

import net.minecraft.client.MinecraftClient;

import static com.google.common.base.Preconditions.checkArgument;

public class ToastMediaInfoListener implements MediaInfoListener {

    private final MinecraftClient client;

    public ToastMediaInfoListener(MinecraftClient client) {
        checkArgument(client != null, "Minecraft client cannot be null!");
        this.client = client;
    }

    @Override
    public void onMediaInfo(MediaInfo mediaInfo) {
        this.client.getToastManager().add(new MediaInfoToast(mediaInfo));
    }

}
