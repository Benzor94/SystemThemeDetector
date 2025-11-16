package hu.benzor.systemthemedetector.internal.monitoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProcessOutputMonitor<T> implements Callable<Void> {

    private final ProcessBuilder processBuilder;
    private final Function<String, T> outputMapper;
    private final Consumer<T> callback;

    @Override
    public Void call() {
        processBuilder.redirectErrorStream(true);
        Process process = null;
        try {
            process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                String line;
                while (!Thread.currentThread().isInterrupted() && (line = reader.readLine()) != null) {
                    callback.accept(outputMapper.apply(line));
                }
            }
        } catch (IOException e) {

            // Log this

        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        
        return null;
    }

}
