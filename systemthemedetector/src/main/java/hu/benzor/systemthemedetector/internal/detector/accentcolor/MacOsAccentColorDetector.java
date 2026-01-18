package hu.benzor.systemthemedetector.internal.detector.accentcolor;

import java.util.Arrays;
import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.internal.command.CommandOutputLineMapper;
import hu.benzor.systemthemedetector.internal.command.FilteredCommandOutputLineMapper;

public final class MacOsAccentColorDetector extends AccentColorDetector {

    private final CommandOutputLineMapper outputLineMapper;
    private static final AccentColor DEFAULT_COLOR = new AccentColor(0, 122, 255);

    public MacOsAccentColorDetector() {
        ProcessBuilder pb = new ProcessBuilder(
            "defaults",
            "read",
            "-g",
            "AppleHighlightColor"
        );
        outputLineMapper = new FilteredCommandOutputLineMapper(pb, true);
    }

    @Override
    protected CommandOutputLineMapper commandOutputMapper() {
        return outputLineMapper;
    }

    @Override
    protected Optional<AccentColor> outputLineToThemeMap(String line) {
        /*
         * Output is of the form d d d s, where d is a decimal number and s is text, e.g.
         * 0.752941 0.964706 0.678431 Green
         * If the accent color is default ("Multicolour") then this command will return an error with empty stdout. 
         */
        if (line.isBlank()) {
            return Optional.of(DEFAULT_COLOR);
        }
        String[] components = line.trim().split(" ");
        if (components.length < 3) {
            return Optional.empty();
        }
        try {
            int[] rgbNumbers = Arrays
                .stream(components)
                .limit(3)
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .mapToInt(AccentColorDetector::decimalToIntRgb)
                .toArray();
            return Optional.of(AccentColor.fromArray(rgbNumbers));             
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
