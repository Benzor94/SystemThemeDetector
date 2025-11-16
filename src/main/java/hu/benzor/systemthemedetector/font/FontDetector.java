package hu.benzor.systemthemedetector.font;

import java.util.Optional;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.monitoring.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme;

public sealed interface FontDetector permits LinuxFontDetector {

    Optional<Theme.Font> getSystemFont();

    ListenerHandle<Theme.Font> registerCallback(Consumer<Optional<Theme.Font>> callback);

}
