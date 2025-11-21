package hu.benzor.systemthemedetector.listeners.api;

import hu.benzor.systemthemedetector.theme.Theme;

public interface ListenerHandle<T extends Theme> {

    boolean isActive();

    void stop();

}
