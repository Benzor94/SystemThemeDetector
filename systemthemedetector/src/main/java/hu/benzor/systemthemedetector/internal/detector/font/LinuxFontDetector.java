package hu.benzor.systemthemedetector.internal.detector.font;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LinuxFontDetector extends FontDetector {
    
    private final FilteredCommandOutputLineMapper outputLineMapper;

    private final Pattern cmdOutputPattern = Pattern.compile(
        "^'([^0-9]+?)(?:,?\\s+)(\\d+(?:\\.\\d+)?)'$"
    );

    public LinuxFontDetector(DesktopEnvironment desktop) {
        ProcessBuilder pb = new ProcessBuilder(
            "gsettings",
            "get",
            getDconfInterfaceSchema(desktop),
            "font-name"
        );
        this.outputLineMapper = new FilteredCommandOutputLineMapper(pb);
    }

    @Override
    protected FilteredCommandOutputLineMapper commandOutputMapper() {
        return outputLineMapper;
    }

    @Override
    protected Optional<Font> outputLineToThemeMap(String line) {
        /*
         * We expect font strings of the scheme "'Noto Sans 10'"" or "'Noto Sans, 10'"" (with the single quotes).
         * It seems that if the font is set from KDE, then the name might be separated from the number by a comma.
         * 
         * The name can be of any number of words, might also contain weight (e.g. Fira Sans Medium), and the
         * number at the end may contain decimal digits.
         */
        Matcher matcher = cmdOutputPattern.matcher(line);
        if (!matcher.matches() || matcher.groupCount() != 2) {
            log.debug("Invalid line format: {}", line);
            return Optional.empty();
        }
        String fontName = matcher.group(1);
        String fontSize = matcher.group(2);
        try {
            Double.parseDouble(fontSize);
            Font font = new Font(fontName, fontSize);
            log.debug("Font determined: {}", font);
            return Optional.of(font);
        } catch (IllegalArgumentException e) {
            log.debug("Font size is not a valid number: {}", fontSize);
            return Optional.empty();
        } 
    }

    private static String getDconfInterfaceSchema(DesktopEnvironment desktop) {
        return switch (desktop) {
            case GNOME, KDE, XFCE, UNKNOWN -> "org.gnome.desktop.interface";
            case CINNAMON -> "org.cinnamon.desktop.interface";
            case MATE -> "org.mate.interface";
        };
    }

}
