package hu.benzor.systemthemedetector;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.internal.themedetector.accentcolor.AccentColorDetector;
import hu.benzor.systemthemedetector.internal.themedetector.accentcolor.FallbackAccentColorDetector;
import hu.benzor.systemthemedetector.internal.themedetector.accentcolor.LinuxAccentColorDetector;
import hu.benzor.systemthemedetector.internal.themedetector.accentcolor.MacOsAccentColorDetector;
import hu.benzor.systemthemedetector.internal.themedetector.accentcolor.WindowsAccentColorDetector;
import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.environment.Platform;
import hu.benzor.systemthemedetector.api.listeners.ListenerHandle;
import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.api.theme.Theme.Mode;
import hu.benzor.systemthemedetector.internal.environment.EnvironmentDetector;
import hu.benzor.systemthemedetector.internal.themedetector.font.FallbackFontDetector;
import hu.benzor.systemthemedetector.internal.themedetector.font.FontDetector;
import hu.benzor.systemthemedetector.internal.themedetector.font.LinuxFontDetector;
import hu.benzor.systemthemedetector.internal.themedetector.font.MacOsFontDetector;
import hu.benzor.systemthemedetector.internal.themedetector.font.WindowsFontDetector;
import hu.benzor.systemthemedetector.internal.themedetector.mode.FallbackModeDetector;
import hu.benzor.systemthemedetector.internal.themedetector.mode.LinuxModeDetector;
import hu.benzor.systemthemedetector.internal.themedetector.mode.MacOsModeDetector;
import hu.benzor.systemthemedetector.internal.themedetector.mode.ModeDetector;
import hu.benzor.systemthemedetector.internal.themedetector.mode.WindowsModeDetector;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemThemeDetector {

    private static final Platform platform;
    private static final DesktopEnvironment desktop;

    private static final FontDetector fontDetector;
    private static final ModeDetector modeDetector;
    private static final AccentColorDetector accentColorDetector;
    private static final List<ListenerHandle<Font>> fontChangeListeners = new CopyOnWriteArrayList<>();
    private static final List<ListenerHandle<Mode>> modeChangeListeners = new CopyOnWriteArrayList<>();
    private static final List<ListenerHandle<AccentColor>> accentColorChangeListeners = new CopyOnWriteArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SystemThemeDetector::onShutdown));
        platform = EnvironmentDetector.getOperatingSystem();
        desktop = platform == Platform.LINUX ? EnvironmentDetector.getDesktopEnvironment() : DesktopEnvironment.UNKNOWN;
        switch (platform) {
            case LINUX -> {
                fontDetector = new LinuxFontDetector(desktop);
                modeDetector = new LinuxModeDetector();
                accentColorDetector = new LinuxAccentColorDetector();
            }
            case WINDOWS -> {
                fontDetector = new WindowsFontDetector();
                modeDetector = new WindowsModeDetector();
                accentColorDetector = new WindowsAccentColorDetector();
            }
            case MACOS -> {
                fontDetector = new MacOsFontDetector();
                modeDetector = new MacOsModeDetector();
                accentColorDetector = new MacOsAccentColorDetector();
            }
            default -> {
                fontDetector = new FallbackFontDetector();
                modeDetector = new FallbackModeDetector();
                accentColorDetector = new FallbackAccentColorDetector();
            }
        };
    }

    public static Platform getPlatform() {
        return platform;
    }

    public static DesktopEnvironment getDesktopEnvironment() {
        return desktop;
    }


    public static Optional<Mode> getCurrentMode() {
        return modeDetector.getCurrentTheme();
    }

    public static Optional<AccentColor> getCurrentAccentColor() {
        return accentColorDetector.getCurrentTheme();
    }

    public static Optional<Font> getCurrentFont() {
        return fontDetector.getCurrentTheme();
    }

    public static ListenerHandle<Mode> onModeChange(Consumer<Optional<Mode>> callback) {
        var handle = modeDetector.registerCallback(callback);
        modeChangeListeners.add(handle);
        return handle;
    }

    public static ListenerHandle<AccentColor> onAccentColorChange(Consumer<Optional<AccentColor>> callback) {
        var handle = accentColorDetector.registerCallback(callback);
        accentColorChangeListeners.add(handle);
        return handle;
    }

    public static ListenerHandle<Font> onFontChange(Consumer<Optional<Font>> callback) {
        var handle = fontDetector.registerCallback(callback);
        fontChangeListeners.add(handle);        
        return handle;
    }

    public static void stopAllModeChangeMonitors() {
        modeChangeListeners.forEach(ListenerHandle::stop);
        modeChangeListeners.clear();
    }

    public static void stopAllAccentColorChangeMonitors() {
        accentColorChangeListeners.forEach(ListenerHandle::stop);
        accentColorChangeListeners.clear();
    }

    public static void stopAllFontChangeMonitors() {
        fontChangeListeners.forEach(ListenerHandle::stop);
        fontChangeListeners.clear();
    }

    private static void onShutdown() {
        modeChangeListeners.forEach(ListenerHandle::stop);
        accentColorChangeListeners.forEach(ListenerHandle::stop);
        fontChangeListeners.forEach(ListenerHandle::stop);
    }

}
