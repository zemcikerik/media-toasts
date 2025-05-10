package dev.zemco.mediatoasts.handlers;

import dev.zemco.mediatoasts.MediaInfo;
import dev.zemco.mediatoasts.MediaInfoListener;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

public class LinuxMediaHandler implements MediaHandler {

    private final MediaInfoListener mediaInfoListener;

    public LinuxMediaHandler(MediaInfoListener mediaInfoListener) {
        checkArgument(mediaInfoListener != null, "Media info listener cannot be null!");
        this.mediaInfoListener = mediaInfoListener;
        this.initialize();
    }

    private native void initialize();
    private native void dispose();

    @SuppressWarnings("unused") // called from native code
    private void onMediaInfoNativeCallback(String artist, String title) {
        // we're creating media info here, not in native code, to avoid class loader issues
        this.mediaInfoListener.onMediaInfo(new MediaInfo(artist, title));
    }

    @Override
    public void close() {
        this.dispose();
    }

    static {
        try {
            // TODO: load correct resource
            // NativeHelpers.loadLibraryFromResource("");

            if (false) {
                // remove me when library load is added
                throw new IOException();
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

}
