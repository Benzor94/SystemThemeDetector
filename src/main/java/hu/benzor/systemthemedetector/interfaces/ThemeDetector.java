package hu.benzor.systemthemedetector.interfaces;

import java.util.function.Consumer;

import hu.benzor.systemthemedetector.dto.Theme;

public interface ThemeDetector {

    Theme getSystemTheme();

    void registerCallback(Consumer<Theme> callback);

}
