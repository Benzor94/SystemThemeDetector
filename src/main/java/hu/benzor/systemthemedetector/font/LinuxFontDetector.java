package hu.benzor.systemthemedetector.font;

import static hu.benzor.systemthemedetector.utils.LinuxUtils.getFontCommand;
import static hu.benzor.systemthemedetector.utils.LinuxUtils.getFontChangeMonitoringCommand;
import static hu.benzor.systemthemedetector.utils.LinuxUtils.getOutputLineFromCommand;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import hu.benzor.systemthemedetector.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.monitoring.ListenerHandle;
import hu.benzor.systemthemedetector.monitoring.ProcessRunner;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class LinuxFontDetector implements FontDetector {

    private final DesktopEnvironment desktopEnvironment;
    private final ExecutorService executorService;


    @Override
    public Optional<Font> getSystemFont() {
        return getOutputLineFromCommand(getFontCommand(desktopEnvironment))
        .map(String::trim)
        .flatMap(LinuxFontDetector::getFontFromString);
    }

    @Override
    public ListenerHandle<Font> registerCallback(Consumer<Optional<Font>> callback) {
        ProcessBuilder pb = new ProcessBuilder(getFontChangeMonitoringCommand(desktopEnvironment));

        return null;
    }

    static Optional<Font> getFontFromString(String fontString) {
        /*
         * We expect font strings of the scheme "'Noto Sans 10'"" or "'Noto Sans, 10'"" (with the single quotes).
         * It seems that if the font is set from KDE, then the name might be separated from the number by a comma
         */
        log.info("Raw font string: {}", fontString);
        if (fontString == null || fontString.length() < 5) {
            // We allow a font like "'A 3'", whose length is 5. Any shorter than this should be invalid.
            log.warn("Invalid font string received: {}", fontString);
            return Optional.empty();
        }
        if (!(fontString.startsWith("'") && fontString.endsWith("'"))) {
            // The dconf key contains the font encased in single quotes, so if this condition is not met, something is wrong.
            log.warn("Invalid font string received: {}", fontString);
            return Optional.empty();
        }
        fontString = fontString.substring(1, fontString.length() - 1); // Cut off the single quotes.
        if (fontString.contains(",")) {
            fontString = fontString.replace(",", "");
        }
        final String finalFontString = fontString;
        int indexOfFirstNumber = IntStream.range(0, finalFontString.length())
        .filter(i -> Character.isDigit(finalFontString.charAt(i)))
        .boxed()
        .findFirst()
        .orElse(-1);
        if (indexOfFirstNumber < 0) {
            return Optional.empty();
        }
        String fontName = finalFontString.substring(0, indexOfFirstNumber).trim();
        String fontSize = finalFontString.substring(indexOfFirstNumber, finalFontString.length());
        log.info("Font name and size detected: {}, {}", fontName, fontSize);
        return Optional.of(new Font(fontName, fontSize));
    }

    private Optional<Font> monitoringOutputMapper(String output) {

        if (output == null || !output.startsWith("font-name:")) {
            return Optional.empty();
        }

    } 

}
