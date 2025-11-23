package hu.benzor.systemthemedetector.internal.themedetector;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.api.listeners.ListenerHandle;
import hu.benzor.systemthemedetector.api.listeners.ListenerHandleImpl;
import hu.benzor.systemthemedetector.api.theme.Theme;
import hu.benzor.systemthemedetector.internal.listeners.Scheduler;
import hu.benzor.systemthemedetector.internal.listeners.ThemeChangeListener;
import hu.benzor.systemthemedetector.internal.themedetector.accentcolor.AccentColorDetector;
import hu.benzor.systemthemedetector.internal.themedetector.font.FontDetector;
import hu.benzor.systemthemedetector.internal.themedetector.mode.ModeDetector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract sealed class ThemeDetector<T extends Theme>
    permits FontDetector, AccentColorDetector, ModeDetector {
    
    
    public Optional<T> getCurrentTheme() {
        Optional<T> theme = getTheme();
        log.info("Current {} is: {}.", type().getSimpleName().toLowerCase(), theme);
        return theme;
    }

    public ListenerHandle<T> registerCallback(Consumer<Optional<T>> callback) {
        ThemeChangeListener<T> listener = new ThemeChangeListener<>(type(), this::getTheme, callback);
        ScheduledFuture<?> task = Scheduler.schedule(listener);
        return new ListenerHandleImpl<>(type(), task);
    }

    protected abstract ProcessBuilder getProcessBuilder();

    protected abstract Optional<T> getThemeFromProcessOutput(String output);

    protected abstract Optional<String> parseProcessOutput(ProcessBuilder processBuilder);

    protected abstract Class<T> type();

    private Optional<T> getTheme() {
        return parseProcessOutput(getProcessBuilder()).flatMap(this::getThemeFromProcessOutput);
    }

}
