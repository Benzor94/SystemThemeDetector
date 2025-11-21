package hu.benzor.systemthemedetector.internal.themedetector.accentcolor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import hu.benzor.systemthemedetector.theme.Theme.AccentColor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public final class LinuxAccentColorDetector extends AccentColorDetector {

    private final Pattern cmdOutputPattern = Pattern.compile("\\(<\\((\\d+(?:\\.\\d+)?), (\\d+(?:\\.\\d+)?), (\\d+(?:\\.\\d+)?)\\)>,\\)");

    @Override
    protected ProcessBuilder getProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "gdbus",
            "call",
            "--session",
            "--timeout=1000",
            "--dest=org.freedesktop.portal.Desktop",
            "--object-path=/org/freedesktop/portal/desktop",
            "--method=org.freedesktop.portal.Settings.ReadOne",
            "org.freedesktop.appearance",
            "accent-color"
        );
        return pb;
    }

    @Override
    protected Optional<AccentColor> getThemeFromProcessOutput(String output) {
        /*
         * We expect strings of the form "(<(d, d, d)>,)" where each d is a decimal number,
         * and we construct the color from these.
         */
        if (output == null) {
            log.warn("Null accent color string received.");
            return Optional.empty();
        }
        Matcher matcher = cmdOutputPattern.matcher(output);
        if (!matcher.matches()) {
            log.warn("Invalid accent color string received: {}.", output);
            return Optional.empty();
        }
        if (matcher.groupCount() != 3) {
            log.warn("Invalid tuple in the color string. The tuple's size must be 3, it was {}.", matcher.groupCount());
            return Optional.empty();
        }
        try {
            int[] rgbColors = IntStream.of(1, 2, 3)
            .mapToObj(matcher::group)
            .mapToDouble(Double::parseDouble)
            .mapToInt(this::srgbToRgb)
            .toArray();
            AccentColor color = AccentColor.fromArray(rgbColors);
            log.info("Accent color determined: {}", color);
            return Optional.of(color);
        } catch (IllegalArgumentException e) {
            log.warn("Members in the color string tuple are invalid: {}", output);
            return Optional.empty();
        }
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return ProcessUtils.getOutputLineFromProcess(processBuilder);
    }

    private int srgbToRgb(double srgb) {
        if (srgb < 0 || srgb > 1) {
            throw new IllegalArgumentException("Color member in sRGB format must be between 0 and 1. It was " + srgb + ".");
        }
        int scaled = (int) Math.round(srgb * 255);
        return Math.max(0, Math.min(scaled, 255));
    }

}
