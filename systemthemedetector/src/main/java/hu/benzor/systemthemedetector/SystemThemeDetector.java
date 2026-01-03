package hu.benzor.systemthemedetector;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.api.environment.DesktopEnvironment;
import hu.benzor.systemthemedetector.api.environment.Platform;
import hu.benzor.systemthemedetector.api.listener.ListenerHandle;
import hu.benzor.systemthemedetector.api.theme.Theme;
import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;
import hu.benzor.systemthemedetector.internal.detector.accentcolor.AccentColorDetector;
import hu.benzor.systemthemedetector.internal.detector.accentcolor.LinuxAccentColorDetector;
import hu.benzor.systemthemedetector.internal.detector.accentcolor.MacOsAccentColorDetector;
import hu.benzor.systemthemedetector.internal.detector.accentcolor.WindowsAccentColorDetector;
import hu.benzor.systemthemedetector.internal.detector.appearance.AppearanceDetector;
import hu.benzor.systemthemedetector.internal.detector.appearance.LinuxAppearanceDetector;
import hu.benzor.systemthemedetector.internal.detector.appearance.MacOsAppearanceDetector;
import hu.benzor.systemthemedetector.internal.detector.appearance.WindowsAppearanceDetector;
import hu.benzor.systemthemedetector.internal.detector.font.FontDetector;
import hu.benzor.systemthemedetector.internal.detector.font.LinuxFontDetector;
import hu.benzor.systemthemedetector.internal.detector.font.MacOsFontDetector;
import hu.benzor.systemthemedetector.internal.detector.font.WindowsFontDetector;
import hu.benzor.systemthemedetector.internal.environment.EnvironmentDetector;


public class SystemThemeDetector {

    private static final List<ListenerHandle<? extends Theme>> listenerHandles = new CopyOnWriteArrayList<>();

    private final Platform platform;
    private final DesktopEnvironment desktop;

    private final AccentColorDetector accentColorDetector;
    private final AppearanceDetector appearanceDetector;
    private final FontDetector fontDetector;

    public SystemThemeDetector() {
        EnvironmentDetector envDetector = new EnvironmentDetector();
        this.platform = envDetector.getPlatform();
        this.desktop = envDetector.getDesktop();

        switch (platform) {
            case LINUX -> {
                accentColorDetector = new LinuxAccentColorDetector();
                appearanceDetector = new LinuxAppearanceDetector();
                fontDetector = new LinuxFontDetector(desktop);
            }
            case MACOS -> {
                accentColorDetector = new MacOsAccentColorDetector();
                appearanceDetector = new MacOsAppearanceDetector();
                fontDetector = new MacOsFontDetector();
            }
            case WINDOWS -> {
                accentColorDetector = new WindowsAccentColorDetector();
                appearanceDetector = new WindowsAppearanceDetector();
                fontDetector = new WindowsFontDetector();
            }
            default -> {
                accentColorDetector = null;
                appearanceDetector = null;
                fontDetector = null;
            }
        }
    }

    public Optional<AccentColor> getAccentColor() {
        return Optional.ofNullable(accentColorDetector).flatMap(x -> x.getTheme());
    }

    public Optional<Appearance> getAppearance() {
        return Optional.ofNullable(appearanceDetector).flatMap(x -> x.getTheme());
    }
    
    public Optional<Font> getFont() {
        return Optional.ofNullable(fontDetector).flatMap(x -> x.getTheme());
    }

    public ListenerHandle<AccentColor> onAccentColorChange(Consumer<AccentColor> callback) {
        ListenerHandle<AccentColor> handle = Optional
            .ofNullable(accentColorDetector)
            .map(x -> x.registerCallback(callback))
            .orElseGet(() -> new ListenerHandle<>(AccentColor.class, null));
        listenerHandles.add(handle);
        return handle;
    }

    public ListenerHandle<Appearance> onAppearanceChange(Consumer<Appearance> callback) {
        ListenerHandle<Appearance> handle = Optional
            .ofNullable(appearanceDetector)
            .map(x -> x.registerCallback(callback))
            .orElseGet(() -> new ListenerHandle<>(Appearance.class, null));
        listenerHandles.add(handle);
        return handle;
    }

    public ListenerHandle<Font> onFontChange(Consumer<Font> callback) {
        ListenerHandle<Font> handle = platform == Platform.LINUX
            ? fontDetector.registerCallback(callback)
            : new ListenerHandle<>(Font.class, null);
        listenerHandles.add(handle);
        return handle;
    }

    public Platform getPlatform() {
        return platform;
    }

    public DesktopEnvironment getDesktop() {
        return desktop;
    }

    public static void stopAllListeners(Class<? extends Theme> type) {
        Arrays
            .stream(listenerHandles.toArray(new ListenerHandle<?>[0]))
            .filter(handle -> type.isAssignableFrom(handle.type()))
            .forEach(
                handle -> {
                    listenerHandles.remove(handle);
                    handle.stop();
                }
            );
    }

    public static void stopAllListeners() {
        stopAllListeners(Theme.class);
    }
}