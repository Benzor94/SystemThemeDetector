package hu.benzor.systemthemedetector.api.listener;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import hu.benzor.systemthemedetector.api.theme.Theme;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListenerHandle<T extends Theme> {

    @NonNull
    @Getter
    private final Class<T> type;
    private final ScheduledFuture<?> task;

    public boolean isActive() {
        return task == null ? false : !task.isDone();
    }

    public void stop() {
        Optional.ofNullable(task).ifPresent(x -> x.cancel(true));
    }
}
