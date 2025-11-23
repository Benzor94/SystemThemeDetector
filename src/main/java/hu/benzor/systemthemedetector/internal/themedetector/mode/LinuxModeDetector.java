package hu.benzor.systemthemedetector.internal.themedetector.mode;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.benzor.systemthemedetector.api.theme.Theme.Mode;
import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public final class LinuxModeDetector extends ModeDetector {

    private final Pattern cmdOutputPattern = Pattern.compile("\\(<uint32 (\\d+)>,\\)");

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
            "color-scheme"
        );
        return pb;
    }

    @Override
    protected Optional<Mode> getThemeFromProcessOutput(String output) {
        /*
         * Here we expect strings of the form "(<uint32 n>,)" where n is an unsigned integer,
         * and we want to extract this integer.
         */
        if (output == null) {
            log.debug("Null mode string received.");
            return Optional.empty();
        }
        Matcher matcher = cmdOutputPattern.matcher(output);
        if (!matcher.matches()) {
            log.debug("Invalid mode string received: {}.", output);
            return Optional.empty();
        }
        String modeString = matcher.group(1);
        try {
            int modeNumber = Integer.parseInt(modeString);
            if (modeNumber < 0 || modeNumber > 2) {
                log.debug("Unrecognited mode number {}.", modeNumber);
            }
            Optional<Mode> mode = Mode.fromId(modeNumber);
            log.debug("Mode determined: {}", mode);
            return mode;
        } catch (NumberFormatException e) {
            log.debug("Invalid mode string received: {}.", output);
            return Optional.empty();
        }
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return ProcessUtils.getOutputLineFromProcess(processBuilder);
    }

}
