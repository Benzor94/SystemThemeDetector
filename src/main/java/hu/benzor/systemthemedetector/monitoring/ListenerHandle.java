package hu.benzor.systemthemedetector.monitoring;

import hu.benzor.systemthemedetector.theme.Theme;

public interface ListenerHandle<T extends Theme> {

    boolean isActive();

    void stop();

    Class<T> type();

}
