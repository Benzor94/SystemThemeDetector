package hu.benzor.systemthemedetector.internal.themedetector;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import hu.benzor.systemthemedetector.internal.listeners.Scheduler;
import hu.benzor.systemthemedetector.internal.listeners.ThemeChangeListener;
import hu.benzor.systemthemedetector.internal.themedetector.accentcolor.AccentColorDetector;
import hu.benzor.systemthemedetector.internal.themedetector.font.FontDetector;
import hu.benzor.systemthemedetector.internal.themedetector.mode.ModeDetector;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandleImpl;
import hu.benzor.systemthemedetector.theme.Theme;

public abstract sealed class ThemeDetector<T extends Theme>
    permits FontDetector, AccentColorDetector, ModeDetector {
    
    private final Supplier<Optional<T>> themSupplier = () -> 
        parseProcessOutput(getProcessBuilder()).flatMap(this::getThemeFromProcessOutput);
    
    public Optional<T> getCurrentTheme() {
        return themSupplier.get();
    }

    public ListenerHandle<T> registerCallback(Consumer<Optional<T>> callback) {
        ThemeChangeListener<T> listener = new ThemeChangeListener<>(themSupplier, callback);
        ScheduledFuture<?> task = Scheduler.schedule(listener);
        return new ListenerHandleImpl<>(task);
    }

    protected abstract ProcessBuilder getProcessBuilder();

    protected abstract Optional<T> getThemeFromProcessOutput(String output);

    protected abstract Optional<String> parseProcessOutput(ProcessBuilder processBuilder);

}
