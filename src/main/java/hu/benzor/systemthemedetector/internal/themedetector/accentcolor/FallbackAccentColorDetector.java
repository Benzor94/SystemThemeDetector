package hu.benzor.systemthemedetector.internal.themedetector.accentcolor;

import java.util.Optional;

import hu.benzor.systemthemedetector.theme.Theme.AccentColor;

public final class FallbackAccentColorDetector extends AccentColorDetector {

    @Override
    protected ProcessBuilder getProcessBuilder() {
        return new ProcessBuilder();
    }

    @Override
    protected Optional<AccentColor> getThemeFromProcessOutput(String output) {
        return Optional.empty();
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return Optional.empty();
    }

}
