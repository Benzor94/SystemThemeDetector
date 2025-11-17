package hu.benzor.systemthemedetector.internal.accentcolor;

import java.util.Optional;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme.Color;

public sealed interface AccentColorDetector permits LinuxAccentColorDetector {

    Optional<Color> getSystemAccentColor();

    ListenerHandle<Color> registerCallback(Consumer<Optional<Color>> callback);

}
