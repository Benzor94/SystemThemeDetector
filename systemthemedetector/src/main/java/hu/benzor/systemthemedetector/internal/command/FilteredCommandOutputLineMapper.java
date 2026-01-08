package hu.benzor.systemthemedetector.internal.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilteredCommandOutputLineMapper {

    private final ProcessBuilder processBuilder;
    private final String filter;

    public FilteredCommandOutputLineMapper(ProcessBuilder processBuilder) {
        this(processBuilder, null);
    }

    public <T> Optional<T> mapLine(Function<String, Optional<T>> lineMapper) {
        Optional<String> line = Optional.empty();
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = process.inputReader()) {
                line = reader.lines().filter(s -> filter == null ? true : s.contains(filter)).findFirst();
            } finally {
                process.waitFor(1, TimeUnit.SECONDS);
                process.destroyForcibly();
            }
        } catch (IOException | IndexOutOfBoundsException e){
            return Optional.empty();
        } catch (InterruptedException ignored) {
            
        }
        return line.flatMap(lineMapper);
    }
}
