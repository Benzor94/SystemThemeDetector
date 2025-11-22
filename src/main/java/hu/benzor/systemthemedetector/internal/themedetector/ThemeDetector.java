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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract sealed class ThemeDetector<T extends Theme>
    permits FontDetector, AccentColorDetector, ModeDetector {
    
    
    public Optional<T> getCurrentTheme() {
        Optional<T> theme = getTheme();
        log.info("Current theme is: {}.", theme);
        return theme;
    }

    public ListenerHandle<T> registerCallback(Consumer<Optional<T>> callback) {
        ThemeChangeListener<T> listener = new ThemeChangeListener<>(this::getTheme, callback);
        ScheduledFuture<?> task = Scheduler.schedule(listener);
        return new ListenerHandleImpl<>(task);
    }

    protected abstract ProcessBuilder getProcessBuilder();

    protected abstract Optional<T> getThemeFromProcessOutput(String output);

    protected abstract Optional<String> parseProcessOutput(ProcessBuilder processBuilder);

    private Optional<T> getTheme() {
        return parseProcessOutput(getProcessBuilder()).flatMap(this::getThemeFromProcessOutput);
    }

}
