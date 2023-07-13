package dev.zemco.mediatoasts.handlers;

import dev.zemco.mediatoasts.MediaInfo;
import dev.zemco.mediatoasts.MediaInfoListener;
import dev.zemco.mediatoasts.NativeHelpers;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

public class WindowsMediaHandler implements MediaHandler {

    private final MediaInfoListener mediaInfoListener;

    public WindowsMediaHandler(MediaInfoListener mediaInfoListener) {
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

    @SuppressWarnings("unused") // called from native code
    public void onUnhandledExceptionNativeCallback(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void close() {
        this.dispose();
    }

    static {
        try {
            NativeHelpers.loadLibraryFromResource("/native/WindowsMediaToasts.dll");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

}
