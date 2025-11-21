package hu.benzor.systemthemedetector.listeners.api;

import java.util.concurrent.ScheduledFuture;

import hu.benzor.systemthemedetector.theme.Theme;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListenerHandleImpl<T extends Theme> implements ListenerHandle<T> {

    private final ScheduledFuture<?> task;

    @Override
    public boolean isActive() {        
        return !task.isDone();
    }

    @Override
    public void stop() {
        task.cancel(true);        
    }

}
