package hu.benzor.systemthemedetector;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.internal.environment.EnvironmentDetector;
import hu.benzor.systemthemedetector.internal.environment.Platform;
import hu.benzor.systemthemedetector.internal.font.FontDetector;
import hu.benzor.systemthemedetector.internal.font.LinuxFontDetector;
import hu.benzor.systemthemedetector.internal.mode.LinuxModeDetector;
import hu.benzor.systemthemedetector.internal.mode.ModeDetector;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme.Color;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import hu.benzor.systemthemedetector.theme.Theme.Mode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemThemeDetector {

    private static final Platform platform;

    private static final FontDetector fontDetector;
    private static final ModeDetector modeDetector;
    private static final List<ListenerHandle<Font>> fontChangeMonitors = new CopyOnWriteArrayList<>();
    private static final List<ListenerHandle<Mode>> modeChangeMonitors = new CopyOnWriteArrayList<>();

    static {
        platform = EnvironmentDetector.getOperatingSystem();
        switch (platform) {
            case UNKNOWN, WINDOWS, MACOS -> {
                fontDetector = new LinuxFontDetector(null);
                modeDetector = new LinuxModeDetector();
            }
            case LINUX -> {
                fontDetector = new LinuxFontDetector(EnvironmentDetector.getDesktopEnvironment());
                modeDetector = new LinuxModeDetector();
            }
            default -> throw new IllegalStateException();
        };
    }


    public static Mode getCurrentMode() {
        return modeDetector.getSystemMode();
    }

    public static Optional<Color> getCurrentAccentColor() {
        // Stub
        return Optional.empty();
    }

    public static Optional<Font> getCurrentFont() {
        // Stub
        return fontDetector.getSystemFont();
    }

    public static ListenerHandle<Mode> onModeChange(Consumer<Mode> callback) {
        var handle = modeDetector.registerCallback(callback);
        modeChangeMonitors.add(handle);
        return handle;
    }

    public static ListenerHandle<Color> onAccentColorChange(Consumer<Optional<Color>> callback) {
        // Stub
        return null;
    }

    public static ListenerHandle<Font> onFontChange(Consumer<Optional<Font>> callback) {
        var handle = fontDetector.registerCallback(callback);
        fontChangeMonitors.add(handle);        
        return handle;
    }

    public static void stopAllModeChangeMonitors() {
        modeChangeMonitors.forEach(ListenerHandle::stop);
        modeChangeMonitors.clear();
    }

    public static void stopAllAccentColorChangeMonitors() {
        // Stub
    }

    public static void stopAllFontChangeMonitors() {
        fontChangeMonitors.forEach(ListenerHandle::stop);
        fontChangeMonitors.clear();
    }

}
