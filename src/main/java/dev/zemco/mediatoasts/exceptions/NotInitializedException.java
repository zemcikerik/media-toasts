package dev.zemco.mediatoasts.exceptions;

@SuppressWarnings("unused") // thrown from native code
public class NotInitializedException extends JniException {

    public NotInitializedException() {
        super("Media handler is not initialized!");
    }

}
