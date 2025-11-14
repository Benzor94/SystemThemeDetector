package hu.benzor.systemthemedetector.environment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentDetector {

    public static OS getOperatingSystem() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName == null) {
            return OS.UNKNOWN;
        }
        if (osName.contains("win")) {
            return OS.WINDOWS;
        }
        if (osName.contains("mac")) {
            return OS.MACOS;
        }
        if (osName.contains("nux")) {
            return OS.LINUX;
        }
        return OS.UNKNOWN;
    }

    public static DE getDesktopEnvironment() {
        String deName = System.getenv("XDG_CURRENT_DESKTOP").toLowerCase();
        if (deName == null) {
            return DE.UNKNOWN;
        }
        if (deName.contains("gnome") || deName.contains("cinnamon") || deName.contains("mate")) {
            return DE.GNOME;
        }
        if (deName.contains("kde")) {
            return DE.KDE;
        }
        if (deName.contains("xfce")) {
            return DE.XFCE;
        }
        return DE.UNKNOWN;
    }

}
