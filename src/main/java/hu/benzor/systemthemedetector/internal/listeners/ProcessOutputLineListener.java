package hu.benzor.systemthemedetector.internal.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@RequiredArgsConstructor
public class ProcessOutputLineListener<T> implements Callable<Void> {

    private final ProcessBuilder processBuilder;
    private final Function<String, T> outputMapper;
    private final Consumer<T> callback;
    private final String filter;

    public ProcessOutputLineListener(
        ProcessBuilder processBuilder,
        Function<String, T> outputMapper,
        Consumer<T> callback
    ) {
        this(processBuilder, outputMapper, callback, null);
    }

    @Override
    public Void call() {
        Process process = null;
        try {
            process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                String line;
                while (!Thread.currentThread().isInterrupted() && (line = reader.readLine()) != null) {
                    if (filter == null || line.contains(filter)) {
                        callback.accept(outputMapper.apply(line));
                    }                    
                }
            }
        } catch (IOException e) {

            log.warn("Something bad happened: {}", e);

        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        
        return null;
    }

}
