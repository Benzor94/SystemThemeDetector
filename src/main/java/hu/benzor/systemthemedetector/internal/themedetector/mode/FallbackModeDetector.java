package hu.benzor.systemthemedetector.internal.themedetector.mode;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.Mode;

public final class FallbackModeDetector extends ModeDetector {

    @Override
    protected ProcessBuilder getProcessBuilder() {
        return new ProcessBuilder();
    }

    @Override
    protected Optional<Mode> getThemeFromProcessOutput(String output) {
        return Optional.empty();
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return Optional.empty();
    }

}
