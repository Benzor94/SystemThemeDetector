package hu.benzor.systemthemedetector.orchestrator;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import hu.benzor.systemthemedetector.environment.EnvironmentDetector;
import hu.benzor.systemthemedetector.environment.Platform;
import hu.benzor.systemthemedetector.font.FontDetector;
import hu.benzor.systemthemedetector.font.LinuxFontDetector;
import hu.benzor.systemthemedetector.monitoring.ListenerHandle;
import hu.benzor.systemthemedetector.theme.Theme.Font;

public class Orchestrator {

    private final FontDetector fontDetector;
    private final List<ListenerHandle<Font>> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public Orchestrator() {
        Platform platform = EnvironmentDetector.getOperatingSystem();
        switch (platform) {
            case WINDOWS -> throw new RuntimeException("Not implemented yet");
            case MACOS -> throw new RuntimeException("Not implemented yet");
            case LINUX -> {
                fontDetector = new LinuxFontDetector(EnvironmentDetector.getDesktopEnvironment(), executorService);
            }
            default -> throw new RuntimeException("Not implemented yet");
        }
    }

    public Optional<Font> getSystemFont() {
        return fontDetector.getSystemFont();
    }

    public ListenerHandle<Font> registerCallback(Consumer<Optional<Font>> callback) {

    }

}
