package dev.zemco.mediatoasts;

import java.util.ArrayList;
import java.util.List;

import static org.spongepowered.include.com.google.common.base.Preconditions.checkArgument;

public class MulticastMediaInfoListener implements MediaInfoListener {

    private final List<MediaInfoListener> listeners = new ArrayList<>();

    public void addListener(MediaInfoListener listener) {
        checkArgument(listener != null, "Listener cannot be null!");
        this.listeners.add(listener);
    }

    @Override
    public void onMediaInfo(MediaInfo mediaInfo) {
        this.listeners.forEach(listener -> listener.onMediaInfo(mediaInfo));
    }

    public boolean removeListener(MediaInfoListener listener) {
        return this.listeners.remove(listener);
    }

}
