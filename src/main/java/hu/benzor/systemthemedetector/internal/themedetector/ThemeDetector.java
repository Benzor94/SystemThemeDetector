package hu.benzor.systemthemedetector.internal.themedetector;

import java.util.function.Consumer;

import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme;

public interface ThemeDetector<T extends Theme> {

    T getCurrentTheme();

    ListenerHandle<T> registerCallback(Consumer<T> callback);

}
