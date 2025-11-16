package hu.benzor.systemthemedetector;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.environment.Platform;
import hu.benzor.systemthemedetector.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.environment.EnvironmentDetector;
import hu.benzor.systemthemedetector.font.FontDetector;
import hu.benzor.systemthemedetector.font.LinuxFontDetector;
import hu.benzor.systemthemedetector.monitoring.MonitorHandle;
import hu.benzor.systemthemedetector.theme.Theme.Color;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import hu.benzor.systemthemedetector.theme.Theme.Mode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemThemeDetector {

    private static final Platform platform;

    private static final FontDetector fontDetector;
    private static final List<MonitorHandle<Font>> fontChangeMonitors = new CopyOnWriteArrayList<>();

    static {
        platform = EnvironmentDetector.getOperatingSystem();
        fontDetector = switch (platform) {
            case UNKNOWN -> throw new RuntimeException("Not implemented yet");
            case WINDOWS -> throw new RuntimeException("Not implemented yet");
            case MACOS -> throw new RuntimeException("Not implemented yet");
            case LINUX -> new LinuxFontDetector(EnvironmentDetector.getDesktopEnvironment());
        };
    }


    public static Mode getCurrentMode() {
        // Stub
        return null;
    }

    public static Optional<Color> getCurrentAccentColor() {
        // Stub
        return Optional.empty();
    }

    public static Optional<Font> getCurrentFont() {
        // Stub
        return fontDetector.getSystemFont();
    }

    public static MonitorHandle<Mode> onModeChange(Consumer<Mode> callback) {
        // Stub
        return null;
    }

    public static MonitorHandle<Color> onAccentColorChange(Consumer<Optional<Color>> callback) {
        // Stub
        return null;
    }

    public static MonitorHandle<Font> onFontChange(Consumer<Optional<Font>> callback) {
        var handle = fontDetector.registerCallback(callback);
        fontChangeMonitors.add(handle);        
        return handle;
    }

    public static void stopAllModeChangeMonitors() {
        // Stub
    }

    public static void stopAllAccentColorChangeMonitors() {
        // Stub
    }

    public static void stopAllFontChangeMonitors() {
        fontChangeMonitors.forEach(MonitorHandle::stop);
        fontChangeMonitors.clear();
    }

}
