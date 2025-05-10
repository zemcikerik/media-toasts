package dev.zemco.mediatoasts;

import org.lwjgl.system.Platform;

import java.util.List;

public class PlatformChecker {

    public boolean isSupportedPlatform() {
        return List.of(Platform.WINDOWS, Platform.LINUX).contains(Platform.get())
            && Platform.getArchitecture() == Platform.Architecture.X64;
    }

    public boolean isLinux() {
        return Platform.get() == Platform.LINUX;
    }

}
