package hu.benzor.systemthemedetector.internal.scheduler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.Test;

public class SchedulerTest {
    @Test
    void testSchedule() {
        AtomicInteger counter = new AtomicInteger();
        Runnable task = () -> counter.addAndGet(1);
        Scheduler.schedule(task);
        
        await()
            .atMost(1200, TimeUnit.MILLISECONDS)
            .until(() -> counter.get() == 1);
    }

    @Test
    void testScheduleMultiple() {
        AtomicInteger counter = new AtomicInteger();
        Runnable task = () -> counter.addAndGet(1);
        Scheduler.schedule(task);
        Scheduler.schedule(task);

        await()
            .atMost(1200, TimeUnit.MILLISECONDS)
            .until(() -> counter.get() == 2);
    }

    @Test
    void testScheduleExecutedMultipleTimes() {
        AtomicInteger counter = new AtomicInteger();
        Runnable task = () -> counter.addAndGet(1);
        Scheduler.schedule(task);

        await()
            .atMost(3200, TimeUnit.MILLISECONDS)
            .until(() -> counter.get() == 3);
    }

    @Test
    void testStopSchedule() {
        AtomicInteger counter = new AtomicInteger();
        Runnable task = () -> counter.addAndGet(1);
        ScheduledFuture<?> future = Scheduler.schedule(task);

        await()
            .atMost(2100, TimeUnit.MILLISECONDS)
            .until(() -> counter.get() == 2);
        future.cancel(true);
        await()
            .during(2, TimeUnit.SECONDS)            
            .until(() -> counter.get() == 2);
    }
}
