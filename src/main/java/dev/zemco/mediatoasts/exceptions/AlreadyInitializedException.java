package dev.zemco.mediatoasts.exceptions;

@SuppressWarnings("unused") // thrown from native code
public class AlreadyInitializedException extends JniException {

    public AlreadyInitializedException() {
        super("Media handler is already initialized! " +
            "This can happen when there are multiple instances of handler that does not support it.");
    }

}
