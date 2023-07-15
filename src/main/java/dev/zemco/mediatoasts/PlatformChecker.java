package dev.zemco.mediatoasts;

import org.lwjgl.system.Platform;

public class PlatformChecker {

    public boolean isSupportedPlatform() {
        return Platform.get() == Platform.WINDOWS && Platform.getArchitecture() == Platform.Architecture.X64;
    }

    public String getPlatform() {
        return Platform.get().getName();
    }

    public String getArchitecture() {
        return Platform.getArchitecture().name();
    }

}
