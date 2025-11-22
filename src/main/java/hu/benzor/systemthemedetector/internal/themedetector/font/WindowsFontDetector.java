package hu.benzor.systemthemedetector.internal.themedetector.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.theme.Theme.Font;

public final class WindowsFontDetector extends FontDetector {

    private static final String FONT_NAME = "Segoe UI";
    private static final int PT_SIZE = 9;

    @Override
    protected ProcessBuilder getProcessBuilder() {
        return new ProcessBuilder();
    }

    @Override
    protected Optional<Font> getThemeFromProcessOutput(String output) {
        /* 
         * On Windows, font is essentially always Segoe UI with a point size of ~9.
         * Scaling should be applied appropriately by both Swing and JavaFX.
         */
        return Optional.of(getDefaultFont());
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return Optional.of("");
    }

    private static Font getDefaultFont() {
        return new Font(FONT_NAME, String.valueOf(PT_SIZE));
    }
}
