package hu.benzor.systemthemedetector.font;

import static hu.benzor.systemthemedetector.utils.LinuxUtils.getFontCommand;
import static hu.benzor.systemthemedetector.utils.LinuxUtils.getOutputLineFromCommand;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import hu.benzor.systemthemedetector.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.environment.EnvironmentDetector;
import hu.benzor.systemthemedetector.listener.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LinuxFontDetector implements FontDetector {

    private static LinuxFontDetector instance;
    private final DesktopEnvironment desktopEnvironment = EnvironmentDetector.getDesktopEnvironment();

    public static LinuxFontDetector getInstance() {
        if (instance == null) {
            instance = new LinuxFontDetector();
        }
        return instance;
    }

    @Override
    public Optional<Font> getSystemFont() {
        return getOutputLineFromCommand(getFontCommand(desktopEnvironment))
        .map(String::trim)
        .flatMap(LinuxFontDetector::getFontFromString);
    }

    @Override
    public ListenerHandle<Font> registerCallback(Consumer<Font> callback) {
        // Stub
        return null;
    }

    static Optional<Theme.Font> getFontFromString(String fontString) {
        /*
         * We expect font strings of the scheme "'Noto Sans 10'"" or "'Noto Sans, 10'"" (with the single quotes).
         * It seems that if the font is set from KDE, then the name might be separated from the number by a comma
         */
        if (fontString == null || fontString.length() < 5) {
            // We allow a font like "'A 3'", whose length is 5. Any shorter than this should be invalid.
            return Optional.empty();
        }
        if (!(fontString.startsWith("'") && fontString.endsWith("'"))) {
            // The dconf key contains the font encased in single quotes, so if this condition is not met, something is wrong.
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
        return Optional.of(new Font(fontName, fontSize));
    }

}
