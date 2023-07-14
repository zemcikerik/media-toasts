package dev.zemco.mediatoasts;

import dev.zemco.mediatoasts.handlers.MediaHandler;
import dev.zemco.mediatoasts.handlers.WindowsMediaHandler;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MediaToastsClientMod implements ClientModInitializer {

    private static MediaToastsClientMod INSTANCE;
    private final Logger logger = LogManager.getLogger();

    private MulticastMediaInfoListener multicastListener;
    private MediaHandler mediaHandler;

    @Override
    public void onInitializeClient() {
        this.logger.info("Initializing!");
        MinecraftClient client = MinecraftClient.getInstance();

        this.multicastListener = new MulticastMediaInfoListener();
        this.multicastListener.addListener(new ToastMediaInfoListener(client.getToastManager()));

        this.mediaHandler = new WindowsMediaHandler(mediaInfo -> {
            // event may be fired on another thread, schedule work on game thread
            client.execute(() -> this.multicastListener.onMediaInfo(mediaInfo));
        });

        INSTANCE = this;
    }

    public void onCloseClient() {
        try {
            this.mediaHandler.close();
        } catch (IOException e) {
            this.logger.error("Failed to close media handler!", e);
            throw new RuntimeException(e);
        }
    }

    public static MediaToastsClientMod getInstance() {
        return INSTANCE;
    }

}
