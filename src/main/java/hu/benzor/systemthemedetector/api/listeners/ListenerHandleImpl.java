package hu.benzor.systemthemedetector.api.listeners;

import java.util.concurrent.ScheduledFuture;

import hu.benzor.systemthemedetector.api.theme.Theme;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListenerHandleImpl<T extends Theme> implements ListenerHandle<T> {
    
    private final Class<T> type;
    private final ScheduledFuture<?> task;

    @Override
    public boolean isActive() {        
        return !task.isDone();
    }

    @Override
    public void stop() {
        task.cancel(true);        
    }

    @Override
    public Class<T> type() {
        return type;
    }

}
