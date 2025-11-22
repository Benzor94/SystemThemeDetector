package hu.benzor.systemthemedetector.internal.themedetector.accentcolor;

import java.util.Optional;

import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import hu.benzor.systemthemedetector.theme.Theme.AccentColor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class WindowsAccentColorDetector extends AccentColorDetector {

    @Override
    protected ProcessBuilder getProcessBuilder() {
        return new ProcessBuilder(
            "reg",
            "query",
            "HKCU\\Software\\Microsoft\\Windows\\DWM",
            "/v",
            "ColorizationColor"
        );
    }

    @Override
    protected Optional<AccentColor> getThemeFromProcessOutput(String output) {
        /*
         * Output: ColorizationColor    REG_DWORD    0xAARRGGBB
         */
        log.debug("Raw accent color string: {}", output);
        if (output == null) {
            log.debug("Accent color string is null.");
            return Optional.empty();
        }
        String[] parts = output.split("REG_DWORD");
        if (parts.length != 2) {
            log.debug("Split accent color string must have length 2.");
            return Optional.empty();
        }
        try {
            String colorId = parts[1].trim();
            if (colorId.length() != 10) {
                log.debug("Processed accent color string is invalid: {}", colorId);
                return Optional.empty();
            }
            long color = Long.decode(colorId);
            int r = (int) (color >>> 16) & 0xFF;
            int g = (int) (color >>> 8) & 0xFF;
            int b = (int) color & 0xFF;
            return Optional.of(new AccentColor(r, g, b));
        } catch (IllegalArgumentException e) {
            log.debug("Processed accent color string is invalid: {}.", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    protected Optional<String> parseProcessOutput(ProcessBuilder processBuilder) {
        return ProcessUtils.getOutputLineFromProcess(processBuilder, "ColorizationColor");
    }

}
