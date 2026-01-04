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
import hu.benzor.systemthemedetector.internal.detector.DetectorFactory;
import hu.benzor.systemthemedetector.internal.detector.ThemeDetector;
import hu.benzor.systemthemedetector.internal.environment.EnvironmentDetector;


public class SystemThemeDetector {

    private static final List<ListenerHandle<? extends Theme>> listenerHandles = new CopyOnWriteArrayList<>();

    private final Platform platform;
    private final DesktopEnvironment desktop;

    private final Optional<ThemeDetector<AccentColor>> accentColorDetector;
    private final Optional<ThemeDetector<Appearance>> appearanceDetector;
    private final Optional<ThemeDetector<Font>> fontDetector;

    public SystemThemeDetector() {
        this(new DetectorFactory(new EnvironmentDetector()));
    }

    SystemThemeDetector(DetectorFactory detectorFactory) {
        this.platform = detectorFactory.getPlatform();
        this.desktop = detectorFactory.getDesktop();
        this.accentColorDetector = detectorFactory.createAccentColorDetector();
        this.appearanceDetector = detectorFactory.createAppearanceDetector();
        this.fontDetector = detectorFactory.createFontDetector();
    }

    public Optional<AccentColor> getAccentColor() {
        return accentColorDetector.flatMap(x -> x.getTheme());
    }

    public Optional<Appearance> getAppearance() {
        return appearanceDetector.flatMap(x -> x.getTheme());
    }
    
    public Optional<Font> getFont() {
        return fontDetector.flatMap(x -> x.getTheme());
    }

    public ListenerHandle<AccentColor> onAccentColorChange(Consumer<AccentColor> callback) {
        ListenerHandle<AccentColor> handle = accentColorDetector
            .map(x -> x.registerCallback(callback))
            .orElseGet(() -> new ListenerHandle<>(AccentColor.class, null));
        listenerHandles.add(handle);
        return handle;
    }

    public ListenerHandle<Appearance> onAppearanceChange(Consumer<Appearance> callback) {
        ListenerHandle<Appearance> handle = appearanceDetector
            .map(x -> x.registerCallback(callback))
            .orElseGet(() -> new ListenerHandle<>(Appearance.class, null));
        listenerHandles.add(handle);
        return handle;
    }

    public ListenerHandle<Font> onFontChange(Consumer<Font> callback) {
        ListenerHandle<Font> handle = platform == Platform.LINUX
            ? fontDetector.get().registerCallback(callback)
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