package hu.benzor.systemthemedetector.interfaces;

import java.util.function.Consumer;

import hu.benzor.systemthemedetector.dto.Theme;
import hu.benzor.systemthemedetector.linux.LinuxThemeDetector;

public sealed interface ThemeDetector permits LinuxThemeDetector {

    Theme getSystemTheme();

    void registerCallback(Consumer<Theme> callback);

}
