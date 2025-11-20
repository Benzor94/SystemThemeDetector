package hu.benzor.systemthemedetector.internal.listeners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Scheduler {

    private static final long POLL_INTERVAL = 1000; // in ms

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public static ScheduledFuture<?> schedule(Runnable task) {
        return scheduler.scheduleAtFixedRate(() -> executor.submit(task), 0, POLL_INTERVAL, TimeUnit.MILLISECONDS);
    }

}
