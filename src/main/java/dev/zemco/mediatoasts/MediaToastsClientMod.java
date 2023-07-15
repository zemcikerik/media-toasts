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

    private PlatformChecker platformChecker;
    private MulticastMediaInfoListener multicastListener;
    private MediaHandler mediaHandler;

    @Override
    public void onInitializeClient() {
        this.logger.info("Initializing!");
        MinecraftClient client = MinecraftClient.getInstance();

        this.platformChecker = new PlatformChecker();
        this.multicastListener = new MulticastMediaInfoListener();
        this.multicastListener.addListener(new ToastMediaInfoListener(client.getToastManager()));

        if (this.platformChecker.isSupportedPlatform()) {
            this.openMediaHandler(client);
        } else {
            this.logger.warn("Unsupported platform detected! Media toasts will be disabled.");
        }

        INSTANCE = this;
    }

    private void openMediaHandler(MinecraftClient client) {
        this.mediaHandler = new WindowsMediaHandler(mediaInfo -> {
            // event may be fired on another thread, schedule work on game thread
            client.execute(() -> this.multicastListener.onMediaInfo(mediaInfo));
        });
    }

    public void closeMediaHandler() {
        if (this.mediaHandler == null) {
            return;
        }

        try {
            this.mediaHandler.close();
            this.mediaHandler = null;
        } catch (IOException e) {
            this.logger.error("Failed to close media handler!", e);
            throw new RuntimeException(e);
        }
    }

    public PlatformChecker getPlatformChecker() {
        return this.platformChecker;
    }

    public static MediaToastsClientMod getInstance() {
        return INSTANCE;
    }

}
