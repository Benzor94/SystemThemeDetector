package hu.benzor.systemthemedetector.environment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentDetector {

    public static Platform getOperatingSystem() {
        String osName = System.getProperty("os.name");
        log.info("Operating system is {}.", osName);
        if (osName == null) {
            log.warn("Operating system could not be detected.");
            return Platform.UNKNOWN;
        }
        osName = osName.toLowerCase();
        if (osName.contains("win")) {
            log.info("Operating system identified as Windows.");
            return Platform.WINDOWS;
        }
        if (osName.contains("mac")) {
            log.info("Operating system identified as MacOS.");
            return Platform.MACOS;
        }
        if (osName.contains("nux")) {
            log.info("Operating system identified as Linux.");
            return Platform.LINUX;
        }
        log.warn("Could not match operating system to any valid option.");
        return Platform.UNKNOWN;
    }

    public static DesktopEnvironment getDesktopEnvironment() {
        String deName = System.getenv("XDG_CURRENT_DESKTOP");
        log.info("Desktop environment is {}.", deName);
        if (deName == null) {
            log.warn("Desktop environment could not be detected.");
            return DesktopEnvironment.UNKNOWN;
        }
        deName = deName.toLowerCase();
        if (deName.contains("gnome")) {
            log.info("Desktop environment identified as Gnome.");
            return DesktopEnvironment.GNOME;
        }
        if (deName.contains("cinnamon")) {
            log.info("Desktop environment identified as Cinnamon.");
            return DesktopEnvironment.CINNAMON;
        }
        if (deName.contains("mate")) {
            log.info("Desktop environment identified as MATE.");
            return DesktopEnvironment.MATE;
        }
        if (deName.contains("kde")) {
            log.info("Desktop environment identified as KDE.");
            return DesktopEnvironment.KDE;
        }
        if (deName.contains("xfce")) {
            log.info("Desktop environment identified as XFCE.");
            return DesktopEnvironment.XFCE;
        }
        log.warn("Could not match desktop environment to any valid option.");
        return DesktopEnvironment.UNKNOWN;
    }

}
