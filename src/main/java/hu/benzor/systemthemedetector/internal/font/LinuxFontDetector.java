package hu.benzor.systemthemedetector.internal.font;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import hu.benzor.systemthemedetector.internal.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.internal.listeners.ProcessOutputLineListener;
import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandleImpl;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class LinuxFontDetector implements FontDetector {

    private final DesktopEnvironment desktop;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public Optional<Font> getSystemFont() {
        return ProcessUtils.getOutputLineFromProcess(getCommandProcessBuilder()).flatMap(this::getFontFromCommandOutput);
    };

    @Override
    public ListenerHandle<Font> registerCallback(Consumer<Optional<Font>> callback) {
        ProcessBuilder pb = getMonitorProcessBuilder();
        Future<Void> task = executorService.submit(
            new ProcessOutputLineListener<>(pb, this::getFontFromMonitorOutput, callback)
        );
        return new ListenerHandleImpl<>(Font.class, task);
    }

    protected ProcessBuilder getCommandProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "gsettings",
            "get",
            getDconfInterfaceSchema(),
            "font-name"
       );
       return pb;
    }

    protected ProcessBuilder getMonitorProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "gsettings",
            "monitor",
            getDconfInterfaceSchema(),
            "font-name"
        );
        return pb;
    }

    protected Optional<Font> getFontFromCommandOutput(String output) {
        /*
         * We expect font strings of the scheme "'Noto Sans 10'"" or "'Noto Sans, 10'"" (with the single quotes).
         * It seems that if the font is set from KDE, then the name might be separated from the number by a comma
         */
        log.info("Raw font string: {}", output);
        if (output == null || output.length() < 5) {
            // We allow a font like "'A 3'", whose length is 5. Any shorter than this should be invalid.
            log.warn("Invalid font string received: {}", output);
            return Optional.empty();
        }
        if (!output.startsWith("'") || !output.endsWith("'")) {
            // The dconf key contains the font encased in single quotes, so if this condition is not met, something is wrong.
            log.warn("Invalid font string received: {}", output);
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
        log.info("Font name and size detected: {}, {}", fontName, fontSize);
        return Optional.of(new Font(fontName, fontSize));
    }

    protected Optional<Font> getFontFromMonitorOutput(String output) {
        /*
         * Here we expect the output string to look like "font-name: 'Noto Sans 10'"
         * or "font-name: 'Noto Sans, 10'".
         */
        if (output == null || !output.startsWith("font-name:")) {
            log.warn("Invalid font string received: {}", output);
            return Optional.empty();
        }
        String truncatedOutput = output.substring(10, output.length()).trim();
        return getFontFromCommandOutput(truncatedOutput);
    }

    private String getDconfInterfaceSchema() {
        return switch (desktop) {
            case GNOME, KDE, XFCE, UNKNOWN -> "org.gnome.desktop.interface";
            case CINNAMON -> "org.cinnamon.desktop.interface";
            case MATE -> "org.mate.interface";
        };
    }

}
