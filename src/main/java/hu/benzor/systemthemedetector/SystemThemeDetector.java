package hu.benzor.systemthemedetector;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.internal.accentcolor.AccentColorDetector;
import hu.benzor.systemthemedetector.internal.accentcolor.LinuxAccentColorDetector;
import hu.benzor.systemthemedetector.internal.environment.EnvironmentDetector;
import hu.benzor.systemthemedetector.internal.environment.Platform;
import hu.benzor.systemthemedetector.internal.font.FontDetector;
import hu.benzor.systemthemedetector.internal.font.LinuxFontDetector;
import hu.benzor.systemthemedetector.internal.mode.LinuxModeDetector;
import hu.benzor.systemthemedetector.internal.mode.ModeDetector;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme.AccentColor;
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
    private static final AccentColorDetector accentColorDetector;
    private static final List<ListenerHandle<Font>> fontChangeListeners = new CopyOnWriteArrayList<>();
    private static final List<ListenerHandle<Mode>> modeChangeListeners = new CopyOnWriteArrayList<>();
    private static final List<ListenerHandle<AccentColor>> accentColorChangeListeners = new CopyOnWriteArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SystemThemeDetector::onShutdown));
        platform = EnvironmentDetector.getOperatingSystem();
        switch (platform) {
            case UNKNOWN, WINDOWS, MACOS -> {
                fontDetector = new LinuxFontDetector(null);
                modeDetector = new LinuxModeDetector();
                accentColorDetector = new LinuxAccentColorDetector();
            }
            case LINUX -> {
                fontDetector = new LinuxFontDetector(EnvironmentDetector.getDesktopEnvironment());
                modeDetector = new LinuxModeDetector();
                accentColorDetector = new LinuxAccentColorDetector();
            }
            default -> throw new IllegalStateException();
        };
    }


    public static Mode getCurrentMode() {
        return modeDetector.getSystemMode();
    }

    public static AccentColor getCurrentAccentColor() {
        return accentColorDetector.getSystemAccentColor();
    }

    public static Font getCurrentFont() {
        return fontDetector.getSystemFont();
    }

    public static ListenerHandle<Mode> onModeChange(Consumer<Mode> callback) {
        var handle = modeDetector.registerCallback(callback);
        modeChangeListeners.add(handle);
        return handle;
    }

    public static ListenerHandle<AccentColor> onAccentColorChange(Consumer<AccentColor> callback) {
        var handle = accentColorDetector.registerCallback(callback);
        accentColorChangeListeners.add(handle);
        return handle;
    }

    public static ListenerHandle<Font> onFontChange(Consumer<Font> callback) {
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
