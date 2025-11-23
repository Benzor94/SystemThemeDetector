package hu.benzor.systemthemedetector.internal.themedetector.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.Font;

public final class FallbackFontDetector extends FontDetector {

    @Override
    protected ProcessBuilder getProcessBuilder() {
        return new ProcessBuilder();
    }

    @Override
    protected Optional<Font> getThemeFromProcessOutput(String output) {
        return Optional.empty();
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return Optional.empty();
    }

}
