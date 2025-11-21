package hu.benzor.systemthemedetector.internal.listeners;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import hu.benzor.systemthemedetector.theme.Theme;

public class ThemeChangeListener<T extends Theme> implements Runnable {

    private final Supplier<Optional<T>> themeSupplier;
    private final Consumer<Optional<T>> callback;

    private final Optional<T> initialTheme;

    public ThemeChangeListener(Supplier<Optional<T>> themeSupplier, Consumer<Optional<T>> callback) {
        this.themeSupplier = themeSupplier;
        this.callback = callback;
        initialTheme = themeSupplier.get();
        if (initialTheme == null) {
            throw new NullPointerException("The initial theme cannot be null.");
        }
    }

    @Override
    public void run() {
        Optional<T> currentTheme = themeSupplier.get();
        if (!initialTheme.equals(currentTheme)) {
            callback.accept(currentTheme);
        }
    }

}
