package hu.benzor.systemthemedetector.internal.environment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentDetector {

    public static Platform getOperatingSystem() {
        String osName = System.getProperty("os.name");

        if (osName == null) {
            log.warn("Platform cannot be determined, as its descriptor was null.");
            return Platform.UNKNOWN;
        }

        log.info("Platform string: {}", osName);

        String os = osName.toLowerCase();

        if (os.contains("win")) {
            log.info("Platform identified as Windows.");
            return Platform.WINDOWS;
        }
        if (os.contains("mac")) {
            log.info("Platform identified as Mac OS.");
            return Platform.MACOS;
        }
        if (os.contains("nux")) {
            log.info("Platform identified as Linux.");
            return Platform.LINUX;
        }
        log.warn("Could not identify operating system.");
        return Platform.UNKNOWN;        
    }

    public static DesktopEnvironment getDesktopEnvironment() {
        String deName = System.getenv("XDG_CURRENT_DESKTOP");

        if (deName == null) {
            log.warn("Desktop environment cannot be determined as its descriptor was null.");
            return DesktopEnvironment.UNKNOWN;
        }

        log.info("Desktop environment string: {}", deName);

        String de = deName.toLowerCase();

        if (de.contains("gnome")) {
            log.info("Desktop environment identified as Gnome.");
            return DesktopEnvironment.GNOME;
        }
        if (de.contains("kde")) {
            log.info("Desktop environment identified as KDE.");
            return DesktopEnvironment.KDE;
        }
        if (de.contains("xfce")) {
            log.info("Desktop environment identified as XFCE.");
            return DesktopEnvironment.XFCE;
        }
        if (de.contains("cinnamon")) {
            log.info("Desktop environment identified as Cinnamon.");
            return DesktopEnvironment.CINNAMON;
        }
        if (de.contains("mate")) {
            log.info("Desktop environment identified as MATE.");
            return DesktopEnvironment.MATE;
        }
        log.warn("Could not identify desktop environment.");
        return DesktopEnvironment.UNKNOWN;
    }

}
