package dev.zemco.mediatoasts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static com.google.common.base.Preconditions.checkArgument;

public final class NativeHelpers {

    public static void loadLibraryFromResource(String resourcePath) throws IOException {
        checkArgument(resourcePath != null, "Resource path cannot be null!");

        String[] nameParts = resourcePath.split("\\.");
        String extension = String.format(".%s", nameParts[nameParts.length - 1]);

        File file = Files.createTempFile(null, extension).toFile();
        file.deleteOnExit();

        try (InputStream is = NativeHelpers.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                String message = String.format("Resource '%s' was not found!", resourcePath);
                throw new IOException(message);
            }

            try (FileOutputStream os = new FileOutputStream(file)) {
                is.transferTo(os);
            }
        }

        System.load(file.getAbsolutePath());
    }

    private NativeHelpers() {
        // prevent instantiation of this class
    }

}
