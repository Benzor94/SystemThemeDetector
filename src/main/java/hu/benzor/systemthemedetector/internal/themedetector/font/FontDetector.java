package hu.benzor.systemthemedetector.internal.themedetector.font;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import hu.benzor.systemthemedetector.internal.listeners.Scheduler;
import hu.benzor.systemthemedetector.internal.listeners.ThemeChangeListener;
import hu.benzor.systemthemedetector.internal.themedetector.ThemeDetector;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.listeners.api.ListenerHandleImpl;
import hu.benzor.systemthemedetector.theme.Theme.Font;

public sealed abstract class FontDetector implements ThemeDetector<Font>
    permits LinuxFontDetector, WindowsFontDetector, MacOsFontDetector, FallbackFontDetector {
    
    private final Supplier<Font> fontSupplier = () -> parseProcessOutput(getProcessBuilder())
    .map(this::getFontFromProcessOutput)
    .orElseGet(Font.Absent::new);
    
    @Override
    public Font getCurrentTheme() {
        return fontSupplier.get();
    }

    @Override
    public ListenerHandle<Font> registerCallback(Consumer<Font> callback) {
        ThemeChangeListener<Font> listener = new ThemeChangeListener<>(fontSupplier, callback);
        ScheduledFuture<?> task = Scheduler.schedule(listener);
        return new ListenerHandleImpl<>(Font.class, task);
    }

    protected abstract ProcessBuilder getProcessBuilder();

    protected abstract Font getFontFromProcessOutput(String output);

    protected abstract Optional<String> parseProcessOutput(ProcessBuilder processBuilder);

}
