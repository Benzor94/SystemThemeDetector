package hu.benzor.systemthemedetector.internal.themedetector.mode;

import java.util.Optional;

import hu.benzor.systemthemedetector.api.theme.Theme.Mode;
import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class WindowsModeDetector extends ModeDetector {

    @Override
    protected ProcessBuilder getProcessBuilder() {
        return new ProcessBuilder(
            "reg",
            "query",
            "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
            "/v",
            "AppsUseLightTheme"
        );
    }

    @Override
    protected Optional<Mode> getThemeFromProcessOutput(String output) {
        /*
         * Output is of the form
         *   AppsUseLightTheme    REG_DWORD    0x1
         */
        log.debug("Raw mode string is: {}.", output);
        if (output == null) {
            log.debug("Null mode string received.");
            return Optional.empty();
        }
        String[] parts = output.split("REG_DWORD");
        if (parts.length != 2) {
            log.debug("Split mode string must have length 2.");
            return Optional.empty();
        }
        String modeId = parts[1].trim();
        try {
            int modeNumber = Integer.parseInt(modeId.substring(modeId.length() - 1));
            return switch (modeNumber) {
                case 0 -> Optional.of(Mode.DARK);
                case 1 -> Optional.of(Mode.LIGHT);
                default -> Optional.empty();
            };
        } catch (IllegalArgumentException e) {
            log.debug("Processed mode string was invalid: {}, {}.", e.getMessage(), modeId);
            return Optional.empty();
        }
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return ProcessUtils.getOutputLineFromProcess(processBuilder, "AppsUseLightTheme");
    }

}
