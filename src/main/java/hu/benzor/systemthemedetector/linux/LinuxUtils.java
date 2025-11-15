package hu.benzor.systemthemedetector.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinuxUtils {

    public static final List<String> GSETTINGS_GET_FONT_COMMAND = List.of(
        "gsettings",
        "get",
        "org.gnome.desktop.interface",
        "font-name"
    );
    public static final List<String> GDBUS_GET_THEME_COMMAND = List.of(
        "gdbus",
        "call",
        "--session",
        "--timeout=1000",
        "--dest=org.freedesktop.portal.Desktop",
        "--object-path=/org/freedesktop/portal/desktop",
        "--method=org.freedesktop.portal.Settings.Read",
        "org.freedesktop.appearance",
        "color-scheme"
    );

    public static final List<String> GDBUS_GET_ACCENT_COLOR_COMMAND = List.of(
        "gdbus",
        "call",
        "--session",
        "--timeout=1000",
        "--dest=org.freedesktop.portal.Desktop",
        "--object-path=/org/freedesktop/portal/desktop",
        "--method=org.freedesktop.portal.Settings.Read",
        "org.freedesktop.appearance",
        "accent-color"
    );

    public static Optional<String> getLineFromCommand(List<String> command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            try (BufferedReader reader = process.inputReader()) {
                String line = reader.readLine();
                return Optional.ofNullable(line);
            }

        } catch (IOException e) {

            return Optional.empty();

        }
    }

}
