package hu.benzor.systemthemedetector.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import hu.benzor.systemthemedetector.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.theme.Theme;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinuxUtils {

    private static final String GNOME_DCONF_INTERFACE_SCHEMA = "org.gnome.desktop.interface";
    private static final String CINNAMON_DCONF_INTERFACE_SCHEMA = "org.cinnamon.desktop.interface";
    private static final String MATE_DCONF_INTERFACE_SCHEMA = "org.mate.interface";
    private static final List<String> GDBUS_GET_MODE_COMMAND = List.of(
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
    private static final List<String> GDBUS_GET_ACCENT_COLOR_COMMAND = List.of(
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

    public static List<String> getFontCommand(DesktopEnvironment de) {
        return List.of(
            "gsettings",
            "get",
            getInterfaceSchemaFromDesktop(de),
            "font-name"
        );
    }

    public static List<String> getModeCommand() {
        return GDBUS_GET_MODE_COMMAND;
    }

    public static List<String> getAccentColorCommand() {
        return GDBUS_GET_ACCENT_COLOR_COMMAND;
    }

    public static Optional<String> getOutputLineFromCommand(List<String> command) {
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

    private static String getInterfaceSchemaFromDesktop(DesktopEnvironment de) {
        return switch (de) {
            case GNOME, KDE, XFCE, UNKNOWN -> GNOME_DCONF_INTERFACE_SCHEMA;
            case CINNAMON -> CINNAMON_DCONF_INTERFACE_SCHEMA;
            case MATE -> MATE_DCONF_INTERFACE_SCHEMA;
        };
    }

}
