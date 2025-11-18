package hu.benzor.systemthemedetector.listeners.api;

import java.util.concurrent.Future;

import hu.benzor.systemthemedetector.internal.listeners.ProcessWrapper;
import hu.benzor.systemthemedetector.theme.Theme;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListenerHandleImpl<T extends Theme> implements ListenerHandle<T> {

    private final Class<T> type;
    private final Future<Void> task;
    private final ProcessWrapper processWrapper;

    @Override
    public boolean isActive() {        
        return !task.isDone();
    }
    @Override
    public void stop() {
        task.cancel(true);
        Process process = processWrapper.process();
        if (process != null) {
            process.destroy();
        }
        
    }
    @Override
    public Class<T> type() {
        return type;
    }

}
