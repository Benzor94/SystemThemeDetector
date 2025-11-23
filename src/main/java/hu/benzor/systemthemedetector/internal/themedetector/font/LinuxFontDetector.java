package hu.benzor.systemthemedetector.internal.themedetector.font;

import java.util.Optional;
import java.util.stream.IntStream;

import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class LinuxFontDetector extends FontDetector {

    private final DesktopEnvironment desktop;

    @Override
    protected ProcessBuilder getProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "gsettings",
            "get",
            getDconfInterfaceSchema(),
            "font-name"
       );
       return pb;
    }

    @Override
    protected Optional<Font> getThemeFromProcessOutput(String output) {
        /*
         * We expect font strings of the scheme "'Noto Sans 10'"" or "'Noto Sans, 10'"" (with the single quotes).
         * It seems that if the font is set from KDE, then the name might be separated from the number by a comma
         */
        log.debug("Raw font string: {}", output);
        if (output == null || output.length() < 5) {
            // We allow a font like "'A 3'", whose length is 5. Any shorter than this should be invalid.
            log.debug("Invalid font string received: {}", output);
            return Optional.empty();
        }
        if (!output.startsWith("'") || !output.endsWith("'")) {
            // The dconf key contains the font encased in single quotes, so if this condition is not met, something is wrong.
            log.debug("Invalid font string received: {}", output);
            return Optional.empty();
        }
        output = output.substring(1, output.length() - 1); // Cut off the single quotes.
        if (output.contains(",")) {
            output = output.replace(",", "");
        }
        final String fontString = output;
        int indexOfFirstNumber = IntStream.range(0, fontString.length())
        .filter(i -> Character.isDigit(fontString.charAt(i)))
        .boxed()
        .findFirst()
        .orElse(-1);
        if (indexOfFirstNumber < 0) {
            return Optional.empty();
        }
        String fontName = fontString.substring(0, indexOfFirstNumber).trim();
        String fontSize = fontString.substring(indexOfFirstNumber, fontString.length());
        log.debug("Font name and size detected: {}, {}", fontName, fontSize);
        return Optional.of(new Font(fontName, fontSize));
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return ProcessUtils.getOutputLineFromProcess(processBuilder);
    }

    private String getDconfInterfaceSchema() {
        return switch (desktop) {
            case GNOME, KDE, XFCE, UNKNOWN -> "org.gnome.desktop.interface";
            case CINNAMON -> "org.cinnamon.desktop.interface";
            case MATE -> "org.mate.interface";
        };
    }

}
