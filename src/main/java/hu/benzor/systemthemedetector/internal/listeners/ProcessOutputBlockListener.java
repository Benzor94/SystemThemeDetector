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
    private final String filter;

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
                     * The output of the dbus-monitor command for accent color is of the form
                     *  variant     struct {
                     *          double d
                     *          double d
                     *          double d
                     *      }
                     * but there are also other lines, so we look for blocks of congruent line that
                     * contain the substring "double".
                     */
                    if (line.contains(filter)) {
                        if (weAreInBlock == false) {
                            weAreInBlock = true;
                        }
                        lineCollector.add(line);
                    } else if (weAreInBlock == true) {
                        callback.accept(outputMapper.apply(lineCollector));
                        lineCollector.clear();
                        weAreInBlock = false;
                    }             
                }
            }
        } catch (IOException e) {

            log.warn("Something bad happened: {}", e);

        } finally {
            log.info("Process: {}", process);
            if (process != null) {
                process.destroy();
            }
        }
        
        return null;
    }
}
