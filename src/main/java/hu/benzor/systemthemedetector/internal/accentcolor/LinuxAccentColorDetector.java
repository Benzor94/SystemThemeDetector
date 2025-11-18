package hu.benzor.systemthemedetector.internal.accentcolor;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import hu.benzor.systemthemedetector.internal.listeners.ProcessOutputBlockListener;
import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandleImpl;
import hu.benzor.systemthemedetector.theme.Theme.Color;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public final class LinuxAccentColorDetector implements AccentColorDetector {

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private final Pattern cmdOutputPattern = Pattern.compile("\\(<\\((\\d+(?:\\.\\d+)?), (\\d+(?:\\.\\d+)?), (\\d+(?:\\.\\d+)?)\\)>,\\)");

    @Override
    public Optional<Color> getSystemAccentColor() {
        return ProcessUtils.getOutputLineFromProcess(getCommandProcessBuilder())
        .flatMap(this::getAccentColorFromCommandOutput);
    }

    @Override
    public ListenerHandle<Color> registerCallback(Consumer<Optional<Color>> callback) {
        ProcessBuilder pb = getListenerProcessBuilder();
        ProcessOutputBlockListener<Optional<Color>> listener = ProcessOutputBlockListener.<Optional<Color>>builder()
        .processBuilder(pb)
        .outputMapper(this::getAccentColorFromListenerOutput)
        .callback(callback)
        .filter("double")
        .build();
        Future<Void> task = executorService.submit(listener);
        return new ListenerHandleImpl<>(Color.class, task);
    }

    protected ProcessBuilder getCommandProcessBuilder() {
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

    protected ProcessBuilder getListenerProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "dbus-monitor",
            "type='signal',interface='org.freedesktop.portal.Settings',arg0='org.freedesktop.appearance',arg1='accent-color'"
        );
        return pb;
    }

    protected Optional<Color> getAccentColorFromCommandOutput(String output) {
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
            Color color = Color.fromArray(rgbColors);
            log.info("Accent color determined: {}", color);
            return Optional.of(color);
        } catch (IllegalArgumentException e) {
            log.warn("Members in the color string tuple are invalid: {}", output);
            return Optional.empty();
        }

    }

    protected Optional<Color> getAccentColorFromListenerOutput(List<String> outputLines) {
        /*
         * The lines are expected to be of the form "double 0.123" with possible whitespace padding,
         * and three lines in total.
         */
        if (outputLines.size() != 3) {
            log.warn("Invalid size for color data. Size must be 3, but it was {}.", outputLines);
            return Optional.empty();
        } try {
            int[] rgbColors = outputLines.stream()
            .map(String::trim)
            .map(
                s -> {
                    if (!s.startsWith("double")) {
                        throw new IllegalArgumentException("Output line must start with \"double\".");
                    }
                    return s.substring(7, s.length());
                }
            )
            .mapToDouble(Double::parseDouble)
            .mapToInt(this::srgbToRgb)
            .toArray();
            Color color = Color.fromArray(rgbColors);
            log.info("Accent color determined: {}.", color);
            return Optional.of(color);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid format in output lines: {}.", outputLines);
            return Optional.empty();
        }

    }

    private int srgbToRgb(double srgb) {
        if (srgb < 0 || srgb > 1) {
            throw new IllegalArgumentException("Color member in sRGB format must be between 0 and 1. It was " + srgb + ".");
        }
        int scaled = (int) Math.round(srgb * 255);
        return Math.max(0, Math.min(scaled, 255));
    }

}
