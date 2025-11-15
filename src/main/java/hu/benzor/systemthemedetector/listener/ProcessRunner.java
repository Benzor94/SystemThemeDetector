package hu.benzor.systemthemedetector.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProcessRunner<T> implements Runnable {

    private final ProcessBuilder processBuilder;
    private final Function<String, T> processOutputMapper;
    private final Consumer<T> callback;

    @Override
    public void run() {
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                String line;
                while (!Thread.currentThread().isInterrupted() && (line = reader.readLine()) != null) {
                    callback.accept(processOutputMapper.apply(line));
                }
            } catch (IOException e) {

            } finally {
                process.destroy();
            }
        } catch (IOException e) {

        }
        
        
    }

}
