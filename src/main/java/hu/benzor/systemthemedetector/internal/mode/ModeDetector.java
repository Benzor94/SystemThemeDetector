package hu.benzor.systemthemedetector.internal.mode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.internal.monitoring.ProcessOutputLineMonitor;
import hu.benzor.systemthemedetector.internal.utils.ProcessUtils;
import hu.benzor.systemthemedetector.monitoring.api.MonitorHandle;
import hu.benzor.systemthemedetector.monitoring.api.MonitorHandleImpl;
import hu.benzor.systemthemedetector.theme.Theme.Mode;

public abstract sealed class ModeDetector permits LinuxModeDetector {

    protected final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public Mode getSystemMode() {
        return ProcessUtils.getOutputLineFromProcess(getCommandProcessBuilder())
        .map(this::getModeFromCommandOutput)
        .orElse(Mode.APP_DEFAULT);
    }

    public MonitorHandle<Mode> registerCallback(Consumer<Mode> callback) {
        ProcessBuilder pb = getMonitorProcessBuilder();
        Future<Void> task = executorService.submit(
            new ProcessOutputLineMonitor<>(pb, this::getModeFromMonitorOutput, callback, "variant")
        );
        return new MonitorHandleImpl<>(Mode.class, task);
    }
    protected abstract ProcessBuilder getCommandProcessBuilder();

    protected abstract ProcessBuilder getMonitorProcessBuilder();

    protected abstract Mode getModeFromCommandOutput(String output);

    protected abstract Mode getModeFromMonitorOutput(String output);

}
