package hu.benzor.systemthemedetector.internal.mode;

import java.util.function.Consumer;

import hu.benzor.systemthemedetector.listeners.api.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme.Mode;

public sealed interface ModeDetector permits LinuxModeDetector {

    public Mode getSystemMode();

    public ListenerHandle<Mode> registerCallback(Consumer<Mode> callback);

}
