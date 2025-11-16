package hu.benzor.systemthemedetector.font;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.monitoring.MonitorHandle;
import hu.benzor.systemthemedetector.monitoring.MonitorHandleImpl;
import hu.benzor.systemthemedetector.monitoring.ProcessOutputMonitor;
import hu.benzor.systemthemedetector.theme.Theme.Font;
import hu.benzor.systemthemedetector.utils.ProcessUtils;

public abstract sealed class FontDetector permits LinuxFontDetector {

    protected final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public Optional<Font> getSystemFont() {
        return ProcessUtils.getOutputLineFromProcess(getCommandProcessBuilder()).flatMap(this::getFontFromCommandOutput);
    };

    public MonitorHandle<Font> registerCallback(Consumer<Optional<Font>> callback) {
        ProcessBuilder pb = getMonitorProcessBuilder();
        Future<Void> task = executorService.submit(
            new ProcessOutputMonitor<>(pb, this::getFontFromMonitorOutput, callback)
        );
        return new MonitorHandleImpl<>(Font.class, task);
    }

    protected abstract ProcessBuilder getCommandProcessBuilder();

    protected abstract ProcessBuilder getMonitorProcessBuilder();

    protected abstract Optional<Font> getFontFromCommandOutput(String output);

    protected abstract Optional<Font> getFontFromMonitorOutput(String output);

}
