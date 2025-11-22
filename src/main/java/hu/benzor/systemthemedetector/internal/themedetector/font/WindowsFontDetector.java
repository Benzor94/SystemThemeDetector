package hu.benzor.systemthemedetector.internal.themedetector.font;

import java.util.Optional;

import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import hu.benzor.systemthemedetector.theme.Theme.Font;

public final class WindowsFontDetector extends FontDetector {

    private static final String FONT_NAME = "Segoe UI";
    private static final double DEFAULT_PT_SIZE = 9.0;

    @Override
    protected ProcessBuilder getProcessBuilder() {
        return new ProcessBuilder(
            "reg",
            "query",
            "HKCU\\Control Panel\\Accessibility",
            "/v",
            "TextScaleFactor"
        );
    }

    @Override
    protected Optional<Font> getThemeFromProcessOutput(String output) {
        /* 
         * On Windows, font is essentially always Segoe UI with a point size of ~9.
         * Scaling should be applied appropriately by both Swing and JavaFX.
         */
        /*
         * TextScaleFactor    REG_DWORD    0x00000064
        */
        if (output == null) {
            return Optional.of(getFallbackFont());
        }
        String[] parts = output.split("REG_DWORD");
        if (parts.length != 2) {
            return Optional.of(getFallbackFont());
        }
        try {
            int scaleFactor = Integer.decode(parts[1].trim());
            Font font = new Font(FONT_NAME, getFontSizeFromScaleFactor(scaleFactor));
            return Optional.of(font);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return Optional.of(getFallbackFont());
        }
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return ProcessUtils.getOutputLineFromProcess(processBuilder, "TextScaleFactor");
    }

    private static Font getFallbackFont() {
        return new Font(FONT_NAME, String.valueOf(DEFAULT_PT_SIZE));
    }

    private static String getFontSizeFromScaleFactor(int scaleFactor) {
        return String.valueOf(DEFAULT_PT_SIZE * (double) scaleFactor / 100);
    }
}
