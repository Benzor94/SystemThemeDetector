package hu.benzor.systemthemedetector.api.listener;

import java.util.concurrent.ScheduledFuture;

import hu.benzor.systemthemedetector.api.theme.Theme;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListenerHandle<T extends Theme> {

    @Getter
    private final Class<T> type;
    private final ScheduledFuture<?> task;

    public boolean isActive() {
        return !task.isDone();
    }

    public void stop() {
        task.cancel(true);
    }
}
