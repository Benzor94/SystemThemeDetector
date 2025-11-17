package hu.benzor.systemthemedetector.internal.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@RequiredArgsConstructor
public class ProcessOutputBlockListener<T> implements Callable<Void> {

    private final ProcessBuilder processBuilder;
    private final Function<List<String>, T> outputMapper;
    private final Consumer<T> callback;
    private final String lineBeforeBlockContainsThis;
    private final String lineAfterBlockContainsThis;

    @Override
    public Void call() {
        Process process = null;
        try {
            process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                String line;
                List<String> lineCollector = new ArrayList<>();
                boolean weAreInBlock = false;
                while (!Thread.currentThread().isInterrupted() && (line = reader.readLine()) != null) {
                    /*
                     * We assume that there is no line which contains both the
                     * substring that marks the beginning of the block and the end of the block.
                     * We also assume there are no nested blocks.
                     */
                    if (line.contains(lineBeforeBlockContainsThis)) {
                        weAreInBlock = true;
                        continue;
                    }
                    if (line.contains(lineAfterBlockContainsThis)) {
                        weAreInBlock = false;
                    }
                    if (weAreInBlock) {
                        lineCollector.add(line);
                    } else if (!lineCollector.isEmpty()) {
                        callback.accept(outputMapper.apply(lineCollector));
                        lineCollector.clear();
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
