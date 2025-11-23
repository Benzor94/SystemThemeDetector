package hu.benzor.systemthemedetector.api.listeners;

import hu.benzor.systemthemedetector.api.theme.Theme;

public interface ListenerHandle<T extends Theme> {

    boolean isActive();

    void stop();

    Class<T> type();

}
