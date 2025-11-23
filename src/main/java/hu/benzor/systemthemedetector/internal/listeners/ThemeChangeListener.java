package hu.benzor.systemthemedetector.internal.listeners;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import hu.benzor.systemthemedetector.api.theme.Theme;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThemeChangeListener<T extends Theme> implements Runnable {

    private final Supplier<Optional<T>> themeSupplier;
    private final Consumer<Optional<T>> callback;

    private Optional<T> previousTheme;

    public ThemeChangeListener(Supplier<Optional<T>> themeSupplier, Consumer<Optional<T>> callback) {
        this.themeSupplier = themeSupplier;
        this.callback = callback;
        previousTheme = themeSupplier.get();
        if (previousTheme == null) {
            throw new NullPointerException("The initial theme cannot be null.");
        }
    }

    @Override
    public void run() {
        Optional<T> currentTheme = themeSupplier.get();
        if (!previousTheme.equals(currentTheme)) {
            previousTheme = currentTheme;
            log.info("Theme changed to: {}.", currentTheme);
            callback.accept(currentTheme);
        }
    }

}
