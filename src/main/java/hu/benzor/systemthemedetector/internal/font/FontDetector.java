package hu.benzor.systemthemedetector.internal.font;

import java.util.Optional;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme.Font;

public sealed interface FontDetector permits LinuxFontDetector {

    Optional<Font> getSystemFont();

    ListenerHandle<Font> registerCallback(Consumer<Optional<Font>> callback);

}
