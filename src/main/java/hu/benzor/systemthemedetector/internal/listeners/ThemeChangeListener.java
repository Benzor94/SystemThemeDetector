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
    private final Class<T> type;

    private Optional<T> previousTheme;

    public ThemeChangeListener(Class<T> type, Supplier<Optional<T>> themeSupplier, Consumer<Optional<T>> callback) {
        this.themeSupplier = themeSupplier;
        this.callback = callback;
        this.type = type;
        previousTheme = themeSupplier.get();
        if (previousTheme == null) {
            throw new NullPointerException("The initial " + type.getSimpleName() + " cannot be null.");
        }
    }

    @Override
    public void run() {
        Optional<T> currentTheme = themeSupplier.get();
        if (!previousTheme.equals(currentTheme)) {
            previousTheme = currentTheme;
            log.info("{} changed to: {}.", type.getSimpleName(), currentTheme);
            callback.accept(currentTheme);
        }
    }

}
