package hu.benzor.systemthemedetector.internal.listener;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import hu.benzor.systemthemedetector.api.theme.Theme;
import hu.benzor.systemthemedetector.api.theme.Theme.AccentColor;
import hu.benzor.systemthemedetector.api.theme.Theme.Appearance;
import hu.benzor.systemthemedetector.api.theme.Theme.Font;

public class ThemeChangeListenerTest {

    @Test
    void testAppearanceChange() {
        Supplier<Optional<Appearance>> themeSupplier = buildThemeSupplier(Appearance.LIGHT, Appearance.DARK);
        @SuppressWarnings("unchecked")
        Consumer<Appearance> callback = Mockito.mock(Consumer.class);

        ThemeChangeListener
            .builder(Appearance.class)
            .themeSupplier(themeSupplier)
            .callback(callback)
            .build()
            .run();
        
        Mockito.verify(callback).accept(Appearance.LIGHT);
        Mockito.verify(callback).accept(Appearance.DARK);
    }

    @Test
    void testAccentColorChange() {
        Supplier<Optional<AccentColor>> themeSupplier = buildThemeSupplier(
            new AccentColor(0, 0, 0),
            new AccentColor(255, 255, 124)
        );
        @SuppressWarnings("unchecked")
        Consumer<AccentColor> callback = Mockito.mock(Consumer.class);

        ThemeChangeListener
            .builder(AccentColor.class)
            .themeSupplier(themeSupplier)
            .callback(callback)
            .build()
            .run();
        
        Mockito.verify(callback).accept(eq(new AccentColor(0, 0, 0)));
        Mockito.verify(callback).accept(eq(new AccentColor(255, 255, 124)));        
    }

    @Test
    void testFontChange() {
        Supplier<Optional<Font>> themeSupplier = buildThemeSupplier(
            new Font("Ubuntu", 10),
            new Font("Adwaita Sans", 11)
        );
        @SuppressWarnings("unchecked")
        Consumer<Font> callback = Mockito.mock(Consumer.class);

        ThemeChangeListener
            .builder(Font.class)
            .themeSupplier(themeSupplier)
            .callback(callback)
            .build()
            .run();
        
        Mockito.verify(callback).accept(eq(new Font("Ubuntu", 10)));
        Mockito.verify(callback).accept(eq(new Font("Adwaita Sans", 11)));
    }

    @Test
    void testChangeFromUnknownIsDetected() {
        Supplier<Optional<Appearance>> themeSupplier = buildThemeSupplier(null, Appearance.DARK);
        @SuppressWarnings("unchecked")
        Consumer<Appearance> callback = Mockito.mock(Consumer.class);

        ThemeChangeListener
            .builder(Appearance.class)
            .themeSupplier(themeSupplier)
            .callback(callback)
            .build()
            .run();
        
        Mockito.verify(callback).accept(Appearance.DARK);
    }

    @Test
    void testCallbackNotExecutedForNoChange() {
        Supplier<Optional<Appearance>> themeSupplier = buildThemeSupplier(Appearance.LIGHT, Appearance.LIGHT);
        @SuppressWarnings("unchecked")
        Consumer<Appearance> callback = Mockito.mock(Consumer.class);

        ThemeChangeListener
            .builder(Appearance.class)
            .themeSupplier(themeSupplier)
            .callback(callback)
            .build()
            .run();
        
        Mockito.verify(callback).accept(eq(Appearance.LIGHT));
    }

    @Test
    void testCallbackNotExecutedForUnknownResult() {
        Supplier<Optional<Appearance>> themeSupplier = buildThemeSupplier(Appearance.LIGHT);
        @SuppressWarnings("unchecked")
        Consumer<Appearance> callback = Mockito.mock(Consumer.class);

        ThemeChangeListener
            .builder(Appearance.class)
            .themeSupplier(themeSupplier)
            .callback(callback)
            .build()
            .run();
        
        Mockito.verify(callback).accept(Appearance.LIGHT);
    }

    @Test
    void testListenerRemembersState() {
        Supplier<Optional<Appearance>> themeSupplier = buildThemeSupplier(Appearance.DARK, Appearance.LIGHT, Appearance.DARK);
        @SuppressWarnings("unchecked")
        Consumer<Appearance> callback = Mockito.mock(Consumer.class);

        ThemeChangeListener<Appearance> listener = ThemeChangeListener
            .builder(Appearance.class)
            .themeSupplier(themeSupplier)
            .callback(callback)
            .build();
        listener.run();
        listener.run();

        verify(callback).accept(Appearance.LIGHT);
        verify(callback, times(2)).accept(Appearance.DARK);
    }

    @SafeVarargs
    private <T extends Theme> Supplier<Optional<T>> buildThemeSupplier(T... themes) {
        Iterator<T> themeIterator = Arrays.asList(themes).iterator();
        return () -> themeIterator.hasNext() ? Optional.ofNullable(themeIterator.next()) : Optional.empty();
    }
}
