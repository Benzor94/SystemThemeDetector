package hu.benzor.systemthemedetector.monitoring.api;

import hu.benzor.systemthemedetector.theme.Theme;

public interface MonitorHandle<T extends Theme> {

    boolean isActive();

    void stop();

    Class<T> type();

}
